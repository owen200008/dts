package com.utopia.data.transfer.admin.hotload;

import java.util.HashSet;
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
import com.utopia.data.transfer.admin.bean.SourceDataMedisProperty;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.JarService;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionPipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.admin.service.TaskService;
import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleSource;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleTarget;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.data.media.SyncRuleType;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
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
    PairService pairService;
    @Autowired
    SourceDataMediaService sourceDataMediaService;
    @Autowired
    TargetDataMediaService targetDataMediaService;
    @Autowired
    LocalCacheManager localCacheManager;
    @Autowired
    JarService jarService;

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

    protected DataMediaRuleSource createDataMediaRuleSource(Long sourceId, Map<Long, SourceDataMediaBean> mapSource, HashSet<Long> existSource) {
        if(existSource.contains(sourceId)) {
            log.error("source loop error!!!!", sourceId);
            return null;
        }

        SourceDataMediaBean sourceDataMediaBean = mapSource.get(sourceId);
        if(Objects.isNull(sourceDataMediaBean)){
            return null;
        }
        existSource.add(sourceId);
        DataMediaRuleSource dataMediaRuleSource = new DataMediaRuleSource();
        dataMediaRuleSource.setId(sourceDataMediaBean.getId());
        dataMediaRuleSource.setDataMediaType(DataMediaType.valueOf(sourceDataMediaBean.getType()));
        dataMediaRuleSource.setNamespace(sourceDataMediaBean.getNamespace());
        dataMediaRuleSource.setValue(sourceDataMediaBean.getTable());
        if(UtopiaStringUtil.isNotBlank(sourceDataMediaBean.getProperty())) {
            SourceDataMedisProperty sourceDataMedisProperty = JSON.parseObject(sourceDataMediaBean.getProperty(), SourceDataMedisProperty.class);
            dataMediaRuleSource.setSources(sourceDataMedisProperty.getLinkSources().stream().map(linkSource-> createDataMediaRuleSource(linkSource, mapSource, existSource)).collect(Collectors.toList()));
        }
        return dataMediaRuleSource;
    }

    public DTSServiceConf getKernelConfig() {
        try {
            //全量获取
            Map<Long, TaskBean> mapTasks = taskSevice.getAll().stream().collect(Collectors.toMap(TaskBean::getId, item -> item));
            List<PipelineBean> ayPipeline = pipelineService.getAll();
            Map<Long, List<RegionPipelineBean>> mapRegion = regionPipelineService.getAll().stream().collect(Collectors.groupingBy(RegionPipelineBean::getPipelineId));
            Map<Long, RegionBean> mapIdToRegion = regionService.getAll().stream().collect(Collectors.toMap(RegionBean::getId, item -> item));
            Map<Long, SyncRuleBean> mapSyncRuleBean = syncRuleService.getAll().stream().collect(Collectors.toMap(SyncRuleBean::getPipelineId, item -> item));
            Map<Long, List<PairBean>> mapPair = pairService.getAll().stream().collect(Collectors.groupingBy(PairBean::getPipelineId));
            Map<Long, SourceDataMediaBean> mapSource = sourceDataMediaService.getAll().stream().collect(Collectors.toMap(SourceDataMediaBean::getId, item -> item));
            Map<Long, TargetDataMediaBean> mapTarget = targetDataMediaService.getAll().stream().collect(Collectors.toMap(TargetDataMediaBean::getId, item -> item));
            List<String> ayJars = jarService.getAll().stream().map(JarBean::getUrl).collect(Collectors.toList());

            List<EntityDesc> ayEntity = entityService.getAll().stream().map(item -> {
                var ret = new EntityDesc();
                ret.setId(item.getId());
                ret.setName(item.getName());
                ret.setType(DataMediaType.valueOf(item.getType()));
                ret.setCreateTime(item.getCreateTime());
                ret.setModifyTime(item.getModifyTime());
                ret.setParams((JSON.parseObject(item.getProperty())));
                return ret;
            }).collect(Collectors.toList());

            Map<Long, EntityDesc> mapEntityDesc = ayEntity.stream().collect(Collectors.toMap(EntityDesc::getId, item -> item));

            List<Pipeline> setPipeline = ayPipeline.stream().map(item -> {
                List<RegionPipelineBean> findRegion = mapRegion.get(item.getId());
                if(CollectionUtils.isEmpty(findRegion)){
                    log.error("no find region {}", JSON.toJSONString(item));
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
                    log.error("stageTypeStringMap error {}", JSON.toJSONString(item));
                    return null;
                }

                TaskBean taskBean = mapTasks.get(item.getTaskId());
                if(Objects.isNull(taskBean) || !taskBean.getValid()){
                    return null;
                }

                Pipeline pipeline = new Pipeline();
                pipeline.setId(item.getId());
                pipeline.setName(item.getName());

                EntityDesc sourceEntityDesc = mapEntityDesc.get(item.getSourceEntityId());
                if(Objects.isNull(sourceEntityDesc)) {
                    log.error("no source entity error {}", JSON.toJSONString(item));
                    return null;
                }

                EntityDesc targetEntityDesc = mapEntityDesc.get(item.getTargetEntityId());
                if(Objects.isNull(targetEntityDesc)) {
                    log.error("no target entity error {}", JSON.toJSONString(item));
                    return null;
                }

                pipeline.setSourceEntityId(item.getSourceEntityId());
                pipeline.setTargetEntityId(item.getTargetEntityId());

                pipeline.setParams(JSON.parseObject(item.getPipelineParams(), PipelineParameter.class));
                pipeline.setStage(stageTypeStringMap);

                SyncRuleBean syncRuleBean = mapSyncRuleBean.get(pipeline.getId());
                if(Objects.isNull(syncRuleBean)){
                    log.error("syncRuleBean error {}", JSON.toJSONString(item));
                    return null;
                }

                List<PairBean> pairBeans = mapPair.get(pipeline.getId());
                if(CollectionUtils.isEmpty(pairBeans)){
                    log.error("pairBeans error {}", JSON.toJSONString(item));
                    return null;
                }

                pipeline.setPairs(pairBeans.stream().map(pair->{
                    DataMediaRulePair dataMediaRulePair = new DataMediaRulePair();

                    HashSet<Long> existSource = new HashSet<>();
                    DataMediaRuleSource dataMediaRuleSource = createDataMediaRuleSource(pair.getSourceDatamediaId(), mapSource, existSource);
                    if(!sourceEntityDesc.getType().equals(dataMediaRuleSource.getDataMediaType())) {
                        return null;
                    }
                    dataMediaRulePair.setSource(dataMediaRuleSource);

                    TargetDataMediaBean targetDataMediaBean = mapTarget.get(pair.getTargetDatamediaId());
                    if(Objects.isNull(targetDataMediaBean)){
                        return null;
                    }

                    DataMediaRuleTarget dataMediaRuleTarget = new DataMediaRuleTarget();
                    dataMediaRuleTarget.setId(targetDataMediaBean.getId());
                    dataMediaRuleTarget.setNamespace(targetDataMediaBean.getNamespace());
                    dataMediaRuleTarget.setValue(targetDataMediaBean.getTable());

                    dataMediaRulePair.setTarget(dataMediaRuleTarget);
                    return dataMediaRulePair;
                }).collect(Collectors.toList()));

                SyncRuleTarget syncRuleTarget = new SyncRuleTarget();
                syncRuleTarget.setSyncRuleType(SyncRuleType.valueOf(syncRuleBean.getSyncRuleType()));
                syncRuleTarget.setNamespace(syncRuleBean.getNamespace());
                syncRuleTarget.setValue(syncRuleBean.getTable());
                syncRuleTarget.setStartGtid(syncRuleBean.getStartGtid());
                pipeline.setSyncRuleTarget(syncRuleTarget);

                return pipeline;
            }).filter(item->item!=null).collect(Collectors.toList());


            DTSServiceConf conf = DTSServiceConf.builder().list(setPipeline)
                    .entityDescs(ayEntity)
                    .jars(ayJars)
                    .build();
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
