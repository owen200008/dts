package com.utopia.data.transfer.core.code.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.utopia.data.transfer.core.code.base.config.DTSConstants;
import com.utopia.data.transfer.core.code.base.datasource.DataSourceService;
import com.utopia.data.transfer.core.code.service.impl.task.select.dispatch.EvaluateClosureParam;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectFactory;
import com.utopia.data.transfer.model.code.NodeTask;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.data.media.DataMedia;
import com.utopia.data.transfer.model.code.data.media.DataMediaPair;
import com.utopia.data.transfer.model.code.data.media.DataMediaSource;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;
import com.utopia.data.transfer.model.code.pipeline.SelectParamter;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
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

            //第一次获取
            //测试构建数据
            DTSServiceConf dtsServiceConf = new DTSServiceConf();

            EntityDesc mainAEntity = new EntityDesc();
            mainAEntity.setId(11L);
            mainAEntity.setName("main-a-test");
            mainAEntity.setZkClusterId(11L);
            mainAEntity.setZkClusters(Arrays.asList("172.26.9.2:2181"));
            mainAEntity.setUrl("jdbc:mysql://main-a-db.blurams.vip:3306/canary1?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
            mainAEntity.setUsername("dev");
            mainAEntity.setPassword("dev");
            mainAEntity.setDriver("com.mysql.cj.jdbc.Driver");

            EntityDesc mainBEntity = new EntityDesc();
            mainBEntity.setId(12L);
            mainBEntity.setName("main-b");
            mainBEntity.setZkClusterId(11L);
            mainBEntity.setZkClusters(Arrays.asList("172.26.9.2:2181"));
            mainBEntity.setUrl("jdbc:mysql://main-b-db.blurams.vip:3306/canary1?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
            mainBEntity.setUsername("dev");
            mainBEntity.setPassword("dev");
            mainBEntity.setDriver("com.mysql.cj.jdbc.Driver");
            dtsServiceConf.setEntityDescs(Arrays.asList(mainAEntity, mainBEntity));


            DataMediaSource dbMediaSourceMainA = new DataMediaSource();
            dbMediaSourceMainA.setId(11L);
            dbMediaSourceMainA.setName("main-a-test");
            dbMediaSourceMainA.setType(DataMediaType.MYSQL);

            DataMediaSource dbMediaSourceMainB = new DataMediaSource();
            dbMediaSourceMainB.setId(12L);
            dbMediaSourceMainB.setName("main_b");
            dbMediaSourceMainB.setType(DataMediaType.MYSQL);


            PipelineParameter pipelineParameter = new PipelineParameter();
            SelectParamter selectParamter = new SelectParamter();
            selectParamter.setDispatchRule("QUEUE");

            EvaluateClosureParam evaluateClosureParam = new EvaluateClosureParam();
            evaluateClosureParam.setQuery("$entity_id");
            evaluateClosureParam.setParams(Arrays.asList("entity_id"));
            selectParamter.setDispatchRuleParam(JSON.toJSONString(evaluateClosureParam));
            pipelineParameter.setSelectParamter(selectParamter);
            pipelineParameter.setEntityName("main-a-test");
            //等于pipelineid
            pipelineParameter.setClientId((short) 1);

            dtsServiceConf.setList(Arrays.asList(Pipeline.builder()
                    .id(11L)
                    .name("a_to_b")
                    .pairs(Arrays.asList(DataMediaPair.builder()
                            .source(DataMedia.builder()
                                    .id(11)
                                    .namespace("canary1")
                                    .value("setting_entity")
                                    .source(dbMediaSourceMainA)
                                    .build())
                            .target(DataMedia.builder()
                                    .id(12)
                                    .namespace("canary2")
                                    .value("setting_entity")
                                    .source(dbMediaSourceMainB)
                                    .build())
                            .build()))
                    .params(pipelineParameter)
                    .build()));
            NodeTask nodeTask = NodeTask.builder()
                    .pipelineId(11L)
                    .stage(Arrays.asList(StageType.SELECT, StageType.EXTRACT, StageType.TRANSFORM, StageType.LOAD))
                    .shutdown(false)
                    .build();
            dtsServiceConf.setTasks(Arrays.asList(nodeTask));


            dtsServiceConf.setMd5Data("1");

            notify(DTSConstants.CONFIG_KEY, JSON.toJSONString(dtsServiceConf));
//            reloadSchedule.schedule(()->{
//                nodeTask.setShutdown(true);
//                dtsServiceConf.setMd5Data("2");
//                notify(DTSConstants.CONFIG_KEY, JSON.toJSONString(dtsServiceConf));
//            }, 10, TimeUnit.SECONDS);


            //notify(DTSConstants.CONFIG_KEY, this.localCacheManager.getConfig(DTSConstants.CONFIG_KEY));

            log.info("Init DTS Step 2 finish!");
            return;
        }while(false);
        reloadSchedule.schedule(()->initStep2(), 1, TimeUnit.SECONDS);
    }

    private synchronized void reloadDTSServiceConf(DTSServiceConf object) throws ExecutionException {
        //配置先加载
        configService.reloadConf(object);

        Map<Long, NodeTask> collect = object.getTasks().stream().collect(Collectors.toMap(NodeTask::getPipelineId, item -> item));
        //先查找不存在的
        tasks.asMap().forEach((pipelineId, item)->{
            if(!collect.containsKey(pipelineId)){
                item.asMap().forEach((stage, task)-> task.shutdown());
                tasks.invalidate(pipelineId);
            }
        });

        //定时获取任务
        for (NodeTask nodeTask : object.getTasks()) {
            LoadingCache<StageType, Task> stageTypeTaskLoadingCache = tasks.get(nodeTask.getPipelineId());
            if(nodeTask.isShutdown()){
                tasks.invalidate(nodeTask.getPipelineId());
                if (stageTypeTaskLoadingCache != null) {
                    log.info("pipeline {} shutdown this pipeline sync tasks = {}", nodeTask.getPipelineId(),
                            stageTypeTaskLoadingCache.asMap().keySet());

                    ConcurrentMap<StageType, Task> stageTypeTaskConcurrentMap = stageTypeTaskLoadingCache.asMap();
                    stageTypeTaskConcurrentMap.forEach((stage, task)->{
                        task.shutdown();
                    });

                    stageTypeTaskConcurrentMap.forEach((stage, task)->{
                        task.waitClose();
                    });
                } else {
                    log.info("pipeline {} is not start sync", nodeTask.getPipelineId());
                }
                continue;
            }
            else if(stageTypeTaskLoadingCache.size() == 0){
                dataSourceService.closePipeline(nodeTask.getPipelineId());
                dbDialectFactory.closePipeline(nodeTask.getPipelineId());
                arbitrateEventService.closePipeline(nodeTask.getPipelineId());

                LoadingCache<StageType, Task> loadingCacheTasks = this.tasks.get(nodeTask.getPipelineId());

                for (StageType stageType : nodeTask.getStage()) {
                    Task task = UtopiaExtensionLoader.getExtensionLoader(Task.class)
                            .getExtension(stageType.name());
                    task.startTask(nodeTask.getPipelineId());
                    loadingCacheTasks.put(stageType, task);
                    log.info("pipeline {} start this task = {} success", nodeTask.getPipelineId(), stageType.name());
                }
            }
            else{
                //如果已经存在，则不做处理
                log.info("pipeline {} same", nodeTask.getPipelineId());
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
                    }
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove (String s) {

    }
}
