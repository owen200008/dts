package com.utopia.data.transfer.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.contants.ErrorCode;
import com.utopia.data.transfer.admin.dao.entity.PairDetail;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.TaskBeanMapper;
import com.utopia.data.transfer.admin.exception.AdminException;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.TaskSevice;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;
import com.utopia.data.transfer.admin.vo.res.PipeDetailRes;
import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.NodeTask;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleSource;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleTarget;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
@Slf4j
public class TaskServiceImpl implements TaskSevice {

    @Autowired
    TaskBeanMapper taskBeanMapper;

    @Autowired
    PipelineService pipelineService;

    // 任务新建状态
    public static final boolean DEFAULT_STATUS = false;

    @Override
    public Integer taskAdd(String name) {
        TaskBean taskBean = new TaskBean();
        taskBean.setName(name);
        taskBean.setCreateTime(LocalDateTime.now());
        taskBean.setModifyTime(LocalDateTime.now());
        taskBean.setValid(DEFAULT_STATUS);
        taskBeanMapper.insert(taskBean);
        return taskBean.getId();
    }

    @Override
    public void taskDelete(Integer id) {
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        taskBeanDal.createCriteria().andIdEqualTo(id);
        taskBeanMapper.deleteByExample(taskBeanDal);
    }

    @Override
    public PageRes<List<TaskBean>> taskList(QueryTaskVo queryTaskVo) {
        Page<Object> page = PageHelper.startPage(queryTaskVo.getPageNum(), queryTaskVo.getPageSize(), true);
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        taskBeanDal.createCriteria().andNameEqualTo(queryTaskVo.getName());
        List<TaskBean> taskBeans = taskBeanMapper.selectByExample(taskBeanDal);
        PageRes<List<TaskBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), taskBeans);
        return pageRes;
    }

    @Override
    public void taskSwitch(Integer id,Integer valid) {
        // 1.先查询状态
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        taskBeanDal.createCriteria().andIdEqualTo(id);
        List<TaskBean> taskBeans = taskBeanMapper.selectByExample(taskBeanDal);
        if (CollectionUtils.isEmpty(taskBeans)){
            throw new AdminException(ErrorCode.TASK_IS_NOT_HAVE);
        }
        boolean boValid = valid.equals(1) ? true : false;
        // 2.当状态不同时更新状态
        if (boValid == (taskBeans.get(0).getValid())){
            TaskBean taskBean = new TaskBean();
            taskBean.setId(id);
            taskBean.setValid(boValid);
            TaskBeanDal taskBeanD = new TaskBeanDal();
            taskBeanD.createCriteria().andValidEqualTo(boValid).andIdEqualTo(id);
            taskBeanMapper.updateByExample(taskBean,taskBeanD);
        }
        // 3.查询其他配置信息
        List<PipeDetailRes> pipeDetailResList = pipelineService.pipelineDetailByTaskId(id);


        List<Pipeline> pipelineList = new ArrayList<>();
        List<NodeTask> nodeTaskList = new ArrayList<>();
        List<EntityDesc> entityDescList = new ArrayList<>();

        pipeDetailResList.stream().forEach(pipe ->{
            Pipeline pipeline = new Pipeline();
            pipeline.setId(pipe.getId().longValue());
            pipeline.setName(pipe.getName());
            PipelineParameter pipelineParameter = null;
            try {
                pipelineParameter = JSONObject.parseObject(pipe.getPipelineParams(), new TypeReference<PipelineParameter>() {});
            } catch (Exception e) {
                log.error("parse pipelineParameter error error reason :{}",e.getMessage());
                throw new AdminException(ErrorCode.PARSE_PIPELINEPARAMS_FAIL);
            }
            pipeline.setParams(pipelineParameter);
            List<DataMediaRulePair> dataMediaRulePairList = new ArrayList<>();
            List<PairDetail> pairList = pipe.getPairList();
            for (PairDetail pairDetail : pairList) {
                DataMediaRuleSource dataMediaRuleSource = DataMediaRuleSource
                        .builder()
                        .id(pairDetail.getSourceDatamediaId())
                        .namespace(pairDetail.getSourceNamespace())
                        .value(pairDetail.getSourceTable())
                        .build();

                DataMediaRuleTarget dataMediaRuleTarget = DataMediaRuleTarget
                        .builder()
                        .id(pairDetail.getSourceDatamediaId())
                        .namespace(pairDetail.getSourceNamespace())
                        .value(pairDetail.getSourceTable())
                        .build();
                DataMediaRulePair dataMediaRulePair = DataMediaRulePair
                        .builder()
                        .source(dataMediaRuleSource)
                        .target(dataMediaRuleTarget)
                        .build();
                dataMediaRulePairList.add(dataMediaRulePair);
                SyncRuleTarget syncRuleTarget = JSONObject.parseObject(pairDetail.getRule(), new TypeReference<SyncRuleTarget>() {});
                pipeline.setSyncRuleTarget(syncRuleTarget);
            }
            pipeline.setPairs(dataMediaRulePairList);

            pipeline.setSourceEntityId(pipe.getSourceEntityId().longValue());
            pipeline.setTargetEntityId(pipe.getTargetEntityId().longValue());

            pipeline.setSourceRegion(pipe.getSourceRegion());
            pipeline.setTargetRegion(pipe.getTargetRegion());

            pipelineList.add(pipeline);


            Map<StageType,String> map = new HashMap<>();
            map.put(StageType.LOAD,pipe.getTargetRegion());
            map.put(StageType.SELECT,pipe.getSourceRegion());

            NodeTask nodeTask = NodeTask.builder()
                    .pipelineId(pipe.getId().longValue())
                    .stage(map)
                    .build();
            nodeTaskList.add(nodeTask);


            EntityDesc entityDesc = new EntityDesc();
        });



        // 4.封装
        // 5.将任务信息放入nacos中等待dts服务拉取
    }


}
