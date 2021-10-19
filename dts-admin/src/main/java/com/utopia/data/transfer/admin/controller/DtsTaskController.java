package com.utopia.data.transfer.admin.controller;

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
        taskSevice.taskAdd(name);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/task/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Long id){
        taskSevice.taskDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/task/list")
    public UtopiaResponseModel<PageRes<List<TaskBean>>> taskList(@Valid QueryTaskVo queryTaskVo){
        PageRes<List<TaskBean>> listPageRes = taskSevice.taskList(queryTaskVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/task/switch")
    public UtopiaResponseModel<Void> taskSwitch(@RequestParam("id")Long id,@RequestParam("valid")Integer valid){
        taskSevice.taskSwitch(id,valid);
        return UtopiaResponseModel.success();
    }
}
