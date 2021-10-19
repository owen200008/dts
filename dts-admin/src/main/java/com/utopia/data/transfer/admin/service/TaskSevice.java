package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;

import java.util.List;

public interface TaskSevice {
    Integer taskAdd(String name);


    void taskDelete(Integer id);

    PageRes<List<TaskBean>> taskList(QueryTaskVo queryTaskVo);

    void taskSwitch(Integer id,Integer valid);


    List<TaskBean> getAll();
}
