package com.utopia.data.transfer.core.code.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.utopia.data.transfer.core.code.base.config.DTSConstants;
import com.utopia.data.transfer.core.code.base.datasource.DataSourceService;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectFactory;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.config.KryoRegister;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.extension.UtopiaExtensionFactory;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.register.center.instance.RegistrationInstance;
import com.utopia.register.center.instance.RegistrationInstanceFactory;
import com.utopia.register.center.sync.LocalCacheManager;
import com.utopia.spring.extension.factory.UtopiaSelfDefineFactory;
import com.utopia.string.UtopiaStringUtil;
import com.utopia.sys.UtopiaShutdownHook;
import com.utopia.unique.serviceid.api.UniqueServiceid;
import com.utopia.utils.NetUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/27
 * @alter_author
 * @alter_date
 */
@Slf4j
@Service
public class DTSMgr implements UtopiaShutdownHook.ShutdownCallbackFunc, LocalCacheManager.NotifyUpdate {

    @Value("${spring.application.region}")
    private String selfRegion;

    /**
     * 上下文模块
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LocalCacheManager localCacheManager;
    @Autowired
    private RegistrationInstanceFactory registrationInstanceFactory;
    @Autowired
    private RegistrationInstance instance;

    @Autowired
    private ConfigService configService;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DbDialectFactory dbDialectFactory;
    @Autowired
    private ArbitrateEventService arbitrateEventService;
    @Autowired
    private TaskFactory taskFactory;

    @SuppressWarnings("AlibabaThreadPoolCreation")
    @Getter
    private ScheduledExecutorService reloadSchedule = Executors.newScheduledThreadPool(1);

    @Autowired
    UniqueServiceid uniqueServiceid;

    private volatile String lastLoadMD5 = "";
    /**
     * 需要重新加载
     */
    private AtomicBoolean needReload = new AtomicBoolean(false);

    /**
     * pipeline
     */
    private LoadingCache<Long, LoadingCache<StageType, Task>> tasks = CacheBuilder.newBuilder().build(new CacheLoader<Long, LoadingCache<StageType, Task>>() {
        @Override
        public LoadingCache<StageType, Task> load (Long key) throws Exception {
            return CacheBuilder.newBuilder().build(new CacheLoader<StageType, Task>() {
                @Override
                public Task load (StageType key) throws Exception {
                    return null;
                }
            });
        }
    });;

    @PostConstruct
    public void init() {
        //使用kryo
        KryoRegister.registerKryo();


        //注册自定义的UtopiaSPIInject
        UtopiaSelfDefineFactory self = (UtopiaSelfDefineFactory) UtopiaExtensionLoader.getExtensionLoader(UtopiaExtensionFactory.class).getExtension("self");
        self.add(ApplicationContext.class,"applicationContext", applicationContext);
        self.add(ApplicationEventPublisher.class,"applicationEventPublisher", applicationEventPublisher);

        //注册注销
        UtopiaShutdownHook.getUtopiaShutdownHook().addDestroyCallback(this);

        //注册
        String publicip = this.instance.getConfig("publicip");
        if(UtopiaStringUtil.isBlank(publicip)){
            this.instance.addConfig("publicip", NetUtils.getPublicIp(DTSConstants.GET_PUBLIC_IP_DOMAIN));
        }
        instance.register();

        log.info("Init DTS Step 1 finish!");

        //同步配置
        //首先是延迟初始化服务id
        reloadSchedule.schedule(() -> {
            log.info("Init UChannel Step 2 start:");
            initStep2();
        }, 1, TimeUnit.SECONDS);
    }

    protected void initStep2(){
        do{
            if(!DubboBootstrap.getInstance().isStarted()){
                break;
            }
            //注册配置更新
            this.localCacheManager.registerNotifyConfig(DTSConstants.CONFIG_KEY, this);

            notify(DTSConstants.CONFIG_KEY, this.localCacheManager.getConfig(DTSConstants.CONFIG_KEY));

            log.info("Init DTS Step 2 finish!");
            return;
        }while(false);
        reloadSchedule.schedule(()->initStep2(), 1, TimeUnit.SECONDS);
    }

    private synchronized void reloadDTSServiceConf(DTSServiceConf object) throws ExecutionException {
        //配置先加载
        configService.reloadConf(object);

        Map<Long, Pipeline> collect = object.getList().stream().collect(Collectors.toMap(Pipeline::getId, item -> item));
        //先查找不存在的
        tasks.asMap().forEach((pipelineId, item)->{
            if(!collect.containsKey(pipelineId)){
                item.asMap().forEach((stage, task)-> {
                    task.shutdown();
                    task.waitClose();
                });
                tasks.invalidate(pipelineId);
            }
        });

        //定时获取任务
        for (Pipeline nodeTask : object.getList()) {
            LoadingCache<StageType, Task> stageTypeTaskLoadingCache = tasks.get(nodeTask.getId());
            if(nodeTask.isShutdown()){
                tasks.invalidate(nodeTask.getId());
                if (stageTypeTaskLoadingCache != null) {
                    log.info("pipeline {} shutdown this pipeline sync tasks = {}", nodeTask.getId(),
                            stageTypeTaskLoadingCache.asMap().keySet());

                    ConcurrentMap<StageType, Task> stageTypeTaskConcurrentMap = stageTypeTaskLoadingCache.asMap();
                    stageTypeTaskConcurrentMap.forEach((stage, task)->{
                        task.shutdown();
                    });

                    stageTypeTaskConcurrentMap.forEach((stage, task)->{
                        task.waitClose();
                    });
                } else {
                    log.info("pipeline {} is not start sync", nodeTask.getId());
                }
                continue;
            }
            else if(stageTypeTaskLoadingCache.size() == 0){
                dataSourceService.closePipeline(nodeTask.getId());
                dbDialectFactory.closePipeline(nodeTask.getId());
                arbitrateEventService.closePipeline(nodeTask.getId());

                LoadingCache<StageType, Task> loadingCacheTasks = this.tasks.get(nodeTask.getId());

                for (Map.Entry<StageType, String> stageTypeStringEntry : nodeTask.getStage().entrySet()) {
                    //分配给自己的任务
                    if(stageTypeStringEntry.getValue().equals(selfRegion)) {
                        Task task = taskFactory.createTaskByType(stageTypeStringEntry.getKey());
                        if(!task.startTask(nodeTask.getId())){
                            log.error("pipeline {} start fail task = {} ", nodeTask.getId(), stageTypeStringEntry.getKey().name());
                            task.shutdown();
                            continue;
                        }
                        loadingCacheTasks.put(stageTypeStringEntry.getKey(), task);
                        log.info("pipeline {} start this task = {} success", nodeTask.getId(), stageTypeStringEntry.getKey().name());
                    }
                }
            }
            else{
                //如果已经存在，则不做处理
                log.info("pipeline {} same", nodeTask.getId());
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void call () {
        tasks.asMap().forEach((pipeline, maps)->{
            maps.asMap().forEach((state, task)->{
                try {
                    task.shutdown();
                } catch (Exception e) {
                    log.error("pipeline {} shutdown task error {}!", pipeline, state, e);
                }
            });
        });

        /**
         * 配置不需要通知
         */
        localCacheManager.removeNotifyConfig(DTSConstants.CONFIG_KEY, this);

        /**
         * 注销实例
         */
        instance.unregister();
    }

    @Override
    public void notify(String s, String s1) {
        if(DTSConstants.CONFIG_KEY.equals(s)){
            //最后一次的md5
            if(UtopiaStringUtil.isBlank(s1)){
                log.error("load config is empty");
                return;
            }
            DTSServiceConf object = JSON.parseObject(s1, DTSServiceConf.class);
            if(object == null || !object.checkIsValid()){
                log.error("load config no valid(%s)", s1);
                return;
            }
            if(lastLoadMD5.equals(object.getMd5Data())){
                return;
            }
            if(Objects.nonNull(object.getMd5Data())){
                lastLoadMD5 = object.getMd5Data();
            }
            //获取配置
            log.info("reloadDTSServiceConf, {} ", s1);

            //设置需要重新加载
            needReload.set(true);

            reloadSchedule.schedule(() -> {
                //
                if(needReload.compareAndSet(true, false)){
                    try {
                        reloadDTSServiceConf(object);
                    } catch (ExecutionException e) {
                        log.error("reloadDTSServiceConf error {}",  JSON.toJSONString(object), e);
                    } catch(Throwable e){
                        log.error("reloadDTSServiceConf error {}",  JSON.toJSONString(object), e);
                    }
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove (String s) {

    }
}
