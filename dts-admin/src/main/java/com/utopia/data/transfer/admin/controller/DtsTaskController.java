package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.service.TaskService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.ResponseModel;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */

@RestController()
@RequestMapping(value = "/dts/")
@Slf4j
public class DtsTaskController {

    @Resource
    TaskService taskSevice;

    @PostMapping("/task/add")
    public UtopiaResponseModel taskAdd(@RequestParam("name")String name){
        Long taskId = taskSevice.taskAdd(name);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId",taskId);
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/task/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Long id){
        taskSevice.taskDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/task/list")
    public UtopiaResponseModel<PageRes<List<TaskBean>>> taskList(QueryTaskVo queryTaskVo){
        PageRes<List<TaskBean>> listPageRes = taskSevice.taskList(queryTaskVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/task/switch")
    public UtopiaResponseModel<Void> taskSwitch(@RequestParam("id")Long id,@RequestParam("valid")Integer valid){
        taskSevice.taskSwitch(id,valid);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/task/modify")
    public UtopiaResponseModel<Void> taskModify(TaskBean taskBean){
        taskSevice.taskModify(taskBean);
        return UtopiaResponseModel.success();
    }
}
