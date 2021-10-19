package com.utopia.data.transfer.admin.controller;

import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.service.TaskService;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;
import com.utopia.data.transfer.admin.vo.ResponseModel;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * describe:
 *  controller
 * @author lxy
 * @date 2021/10/14
 */
@RestController()
@RequestMapping(value = "/dts/")
@Slf4j
public class DtsAdminController {

    @Resource
    EntityService entityService;
    @Resource
    PipelineService pipelineService;
    @Resource
    RegionService regionService;
    @Resource
    TaskService taskSevice;


    @GetMapping("/entity/add")
    public ResponseModel<Integer> entityAdd(@Valid EntityAddVo entityAddVo){
        Integer entityId = entityService.addEntity(entityAddVo);
        return ResponseModel.success(entityId);
    }


    @GetMapping("/entity/delete")
    public ResponseModel<Void> entityDelete(@RequestParam(value = "id",required = true)Integer id ){
        entityService.deleteEntity(id);
        return ResponseModel.success();
    }

    @GetMapping("/entity/get")
    public ResponseModel<EntityRes> entityGet(@RequestParam(value = "id",required = true)Integer id ){
        EntityRes entityById = entityService.getEntityById(id);
        return ResponseModel.success(entityById);
    }

    @GetMapping("/entity/list")
    public ResponseModel<PageRes<List<EntityRes>>> entityList(@Valid QueryEntityVo queryEntityVo){
        PageRes<List<EntityRes>> entityById = entityService.getEntityList(queryEntityVo);
        return ResponseModel.success(entityById);
    }


    @GetMapping("/pipeline/add")
    public ResponseModel pipelineAdd(@Valid PipelineAddVo pipelineAddVo){
        pipelineService.pipelineAdd(pipelineAddVo);
        return ResponseModel.success();
    }

    @GetMapping("/pipeline/list")
    public ResponseModel<List<PipeParamsRes>> pipelineList(@RequestParam(value = "taskId",required = true) Integer id){
        List<PipeParamsRes> pipeParamsRes = pipelineService.pipelineParamsList(id);
        return ResponseModel.success(pipeParamsRes);
    }

    @GetMapping("/pipeline/pair/add")
    public ResponseModel pipelinePairAdd(@Valid PipelinePairAddVo pipelinePairAddVo){
        pipelineService.pipelinePairAdd(pipelinePairAddVo);
        return ResponseModel.success();
    }

    @GetMapping("/pipeline/region/add")
    public ResponseModel pipelineRegionAdd(@Valid PipelineRegionAddVo pipelineRegionAddVo){
        pipelineService.pipelineRegionAdd(pipelineRegionAddVo);
        return ResponseModel.success();
    }

    @GetMapping("/task/add")
    public ResponseModel taskAdd(@RequestParam("name")String name){
        taskSevice.taskAdd(name);
        return ResponseModel.success();
    }

    @GetMapping("/task/delete")
    public ResponseModel<Integer> taskDelete(@RequestParam("id")Long id){
        taskSevice.taskDelete(id);
        return ResponseModel.success();
    }

    @GetMapping("/task/list")
    public ResponseModel<PageRes<List<TaskBean>>> taskList(@Valid QueryTaskVo queryTaskVo){
        PageRes<List<TaskBean>> listPageRes = taskSevice.taskList(queryTaskVo);
        return ResponseModel.success(listPageRes);
    }

    @GetMapping("/task/switch")
    public ResponseModel<Void> taskSwitch(@RequestParam("id")Integer id,@RequestParam("valid")Integer valid){
        taskSevice.taskSwitch(id,valid);
        return ResponseModel.success();
    }
}
