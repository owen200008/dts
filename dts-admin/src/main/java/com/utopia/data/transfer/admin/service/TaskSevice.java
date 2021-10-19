package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;

import java.util.List;

public interface TaskSevice {
    /**
     * 创建任务
     * @param name
     */
    void taskAdd(String name);

    /**
     * 删除任务
     * @param id
     */
    void taskDelete(Long id);

    /**
     * 搜索
     * @param queryTaskVo
     * @return
     */
    PageRes<List<TaskBean>> taskList(QueryTaskVo queryTaskVo);

    /**
     * 任务开启和结束
     * @param id
     * @param valid
     */
    void taskSwitch(Long id, Integer valid);

    /**
     * 获取所有任务
     * @return
     */
    List<TaskBean> getAll();
}
