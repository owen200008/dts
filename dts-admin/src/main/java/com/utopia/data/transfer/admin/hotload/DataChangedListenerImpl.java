package com.utopia.data.transfer.admin.hotload;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.utopia.algorithm.UtopiaAlgorithm;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionPipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import com.utopia.data.transfer.admin.service.TaskService;
import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.data.media.SyncRuleType;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;
import com.utopia.register.center.instance.Registration;
import com.utopia.register.center.sync.LocalCacheManager;
import com.utopia.string.UtopiaStringUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Abstract class for ConfigEventListener. As we think that the md5 value of the in-memory data is
 * the same as the md5 value of the database, although it may be a little different, but it doesn't
 * matter, we will have thread to periodically pull the data in the database.
 *
 * @author huangxiaofeng
 * @since 2.0.0
 */
@Slf4j
@Service
public class DataChangedListenerImpl implements DataChangedListener, InitializingBean, LocalCacheManager.NotifyUpdate {
    @Autowired
    EntityService entityService;
    @Autowired
    TaskService taskSevice;
    @Autowired
    RegionService regionService;
    @Autowired
    RegionPipelineService regionPipelineService;
    @Autowired
    PipelineService pipelineService;
    @Autowired
    Registration registration;
    @Autowired
    SyncRuleService syncRuleService;
    @Autowired
    LocalCacheManager localCacheManager;

    @Value("${utopia.channel.datachange.exporttime:60000}")
    Integer exporttime;

    /**
     * 缓存数据
     */
    volatile String cacheData;

    /**
     * 需要重新加载
     */
    private AtomicBoolean needWrite = new AtomicBoolean(false);
    @SuppressWarnings("AlibabaThreadPoolCreation")
    private ScheduledExecutorService reloadSchedule = Executors.newScheduledThreadPool(1);

    @Override
    public final void afterPropertiesSet() {
        localCacheManager.registerNotifyConfig(PathConstants.CONFIG_KEY, this);
        this.cacheData = localCacheManager.getConfig(PathConstants.CONFIG_KEY);

        String checkMd5 = null;
        if(UtopiaStringUtil.isNotBlank(this.cacheData)){
            DTSServiceConf registerServiceConf = JSON.parseObject(this.cacheData, DTSServiceConf.class);
            checkMd5 = registerServiceConf.getMd5Data();
        }

        //第一次不用datachange
        DTSServiceConf kernelConfig = getKernelConfig();
        if(kernelConfig != null){
            if(!kernelConfig.getMd5Data().equals(checkMd5)){
                registration.config(PathConstants.CONFIG_KEY, JSON.toJSONString(kernelConfig));
            }
        }
    }

    @Override
    public void onDataChanged(String groupKey) {
        //默认延迟配置1分钟配置一次
        needWrite.set(true);

        reloadSchedule.schedule(() -> {
            if(needWrite.compareAndSet(true, false)){
                registration.config(groupKey, buildAllConfig(groupKey));
            }
        }, exporttime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String buildAllConfig(String groupKey) {
        return JSON.toJSONString(getKernelConfig());
    }


    protected RegionPipelineBean getRegionByType(List<RegionPipelineBean> findRegion, final StageType mode){
        for (RegionPipelineBean regionBean : findRegion) {
            if(regionBean.getMode().equals(mode.name())){
                return regionBean;
            }
        }
        return null;
    }
    public DTSServiceConf getKernelConfig() {
        try {
            //全量获取
            Map<Long, TaskBean> mapTasks = taskSevice.getAll().stream().collect(Collectors.toMap(TaskBean::getId, item -> item));
            List<PipelineBean> ayPipeline = pipelineService.getAll();
            Map<Long, List<RegionPipelineBean>> mapRegion = regionPipelineService.getAll().stream().collect(Collectors.groupingBy(RegionPipelineBean::getPipelineId));
            Map<Long, RegionBean> mapIdToRegion = regionService.getAll().stream().collect(Collectors.toMap(RegionBean::getId, item -> item));
            Map<Long, SyncRuleBean> mapSyncRuleBean = syncRuleService.getAll().stream().collect(Collectors.toMap(SyncRuleBean::getPipelineId, item -> item));

            List<EntityDesc> ayEntity = entityService.getAll().stream().map(item -> {
                var ret = new EntityDesc();
                ret.setId(item.getId());
                ret.setName(item.getName());
                ret.setType(DataMediaType.valueOf(item.getType()));
                ret.setEncode(item.getEncode());
                ret.setCreateTime(item.getCreateTime());
                ret.setModifyTime(item.getModifyTime());
                ret.setUrl(item.getUrl());
                ret.setUsername(item.getUsername());
                ret.setPassword(item.getPassword());
                ret.setDriver(item.getDriver());
                ret.setParams((JSON.parseObject(item.getProperty())));
                return ret;
            }).collect(Collectors.toList());

            List<Pipeline> setPipeline = ayPipeline.stream().map(item -> {
                List<RegionPipelineBean> findRegion = mapRegion.get(item.getId());
                if(CollectionUtils.isEmpty(findRegion)){
                    return null;
                }
                Map<StageType, String> stageTypeStringMap = StageType.checkAndChange(type -> {
                    RegionPipelineBean regionByType = getRegionByType(findRegion, type);
                    if (Objects.isNull(regionByType)) {
                        return null;
                    }
                    return mapIdToRegion.get(regionByType.getRegionId()).getRegion();
                });
                if(CollectionUtils.isEmpty(stageTypeStringMap)){
                    return null;
                }

                TaskBean taskBean = mapTasks.get(item.getTaskId());
                if(Objects.isNull(taskBean) || !taskBean.getValid()){
                    return null;
                }

                Pipeline pipeline = new Pipeline();
                pipeline.setId(item.getId());
                pipeline.setName(item.getName());

                pipeline.setSourceEntityId(item.getSourceEntityId());
                pipeline.setTargetEntityId(item.getTargetEntityId());

                pipeline.setParams(JSON.parseObject(item.getPipelineParams(), PipelineParameter.class));
                pipeline.setStage(stageTypeStringMap);

                SyncRuleBean syncRuleBean = mapSyncRuleBean.get(pipeline);
                if(Objects.isNull(syncRuleBean)){
                    return null;
                }

                SyncRuleTarget syncRuleTarget = new SyncRuleTarget();
                syncRuleTarget.setSyncRuleType(SyncRuleType.valueOf(syncRuleBean.getSyncRuleType()));
                syncRuleTarget.setNamespace(syncRuleBean.getNamespace());
                syncRuleTarget.setValue(syncRuleBean.getTable());
                syncRuleTarget.setStartGtid(syncRuleBean.getStartGtid());
                pipeline.setSyncRuleTarget(syncRuleTarget);

                return pipeline;
            }).filter(item->item!=null).collect(Collectors.toList());


            DTSServiceConf conf = DTSServiceConf.builder().list(setPipeline)
                    .entityDescs(ayEntity).build();
            conf.setMd5Data(UtopiaAlgorithm.md5Encode(JSON.toJSONString(conf)));
            return conf;
        } catch (Exception e) {
            log.warn("updateConfigCache error.", e);
        }
        return null;
    }

    @Override
    public void notify(String s, String s1) {
        this.cacheData = s1;
    }

    @Override
    public void remove(String s) {

    }
}
