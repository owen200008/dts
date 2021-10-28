package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.aspect.EventCut;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.TaskBeanMapper;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.TaskService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskBeanMapper taskBeanMapper;

    @Autowired
    PipelineService pipelineService;

    // 任务新建状态
    public static final boolean DEFAULT_STATUS = false;

    @Override
    public Long taskAdd(String name) {
        TaskBean taskBean = new TaskBean();
        taskBean.setName(name);
        taskBean.setCreateTime(LocalDateTime.now());
        taskBean.setModifyTime(LocalDateTime.now());
        taskBean.setValid(DEFAULT_STATUS);
        taskBeanMapper.insert(taskBean);
        return taskBean.getId();
    }

    @Override
    public void taskDelete(Long id) {
        //检查所有pipeline是否都已经删除
        List<PipelineBean> pipeDetailRes = pipelineService.pipelineParamsList(id);
        if (!CollectionUtils.isEmpty(pipeDetailRes)) {
            throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
        }
        taskBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageRes<List<TaskBean>> taskList(QueryTaskVo queryTaskVo) {
        Page<Object> page = PageHelper.startPage(queryTaskVo.getPageNum(), queryTaskVo.getPageSize(), true);
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        if (!StringUtils.isEmpty(queryTaskVo.getName())) {
            taskBeanDal.createCriteria().andNameEqualTo(queryTaskVo.getName());
        }
        taskBeanDal.setOrderByClause(" create_time");
        List<TaskBean> taskBeans = taskBeanMapper.selectByExample(taskBeanDal);
        PageRes<List<TaskBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), taskBeans);
        return pageRes;
    }

    @Override
    @EventCut(key = PathConstants.CONFIG_KEY)
    public int taskSwitch(Long id, Integer valid) {
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        taskBeanDal.createCriteria().andIdEqualTo(id);
        TaskBean taskBean = new TaskBean();
        taskBean.setValid(valid > 0 ? true : false);
        return taskBeanMapper.updateByExampleSelective(taskBean, taskBeanDal);
    }

    @Override
    public List<TaskBean> getAll() {
        return taskBeanMapper.selectByExample(new TaskBeanDal());
    }

    @Override
    public void taskModify(TaskBean taskBean) {
        TaskBeanDal taskBeanDal = new TaskBeanDal();
        taskBeanDal.createCriteria().andIdEqualTo(taskBean.getId());
        taskBeanMapper.updateByExampleSelective(taskBean,taskBeanDal);
    }
}
