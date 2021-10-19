package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.vo.ResponseModel;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */

@RestController()
@RequestMapping(value = "/dts/")
@Slf4j
public class DtsPipelineController {

    @Resource
    PipelineService pipelineService;

    @PostMapping("/pipeline/add")
    public UtopiaResponseModel pipelineAdd(@Valid PipelineAddVo pipelineAddVo){
        pipelineService.pipelineAdd(pipelineAddVo);
        return UtopiaResponseModel.success();
    }


    @PostMapping("/pipeline/pair/add")
    public UtopiaResponseModel pipelinePairAdd(@Valid PipelinePairAddVo pipelinePairAddVo){
        pipelineService.pipelinePairAdd(pipelinePairAddVo);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/pipeline/region/add")
    public UtopiaResponseModel pipelineRegionAdd(@Valid PipelineRegionAddVo pipelineRegionAddVo){
        pipelineService.pipelineRegionAdd(pipelineRegionAddVo);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/pipeline/syncRule/add")
    public UtopiaResponseModel pipelineSyncRuleAdd(SyncRuleBean syncRule){
        if (Objects.isNull(syncRule)){
            UtopiaResponseModel.fail(ErrorCode.CANAL_PARSE_ERROR);
        }
        pipelineService.pipelineSyncRuleAdd(syncRule);
        return UtopiaResponseModel.success();
    }


    @PostMapping("/pipeline/list")
    public UtopiaResponseModel<List<JSONObject>> pipelineList(@RequestParam(value = "taskId",required = true) Long id){
        List<PipelineBean> pipelineBeans = pipelineService.pipelineParamsList(id);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        pipelineBeans.stream().forEach(pipe ->{
            Long pipeId = pipe.getId();
            String name = pipe.getName();
            String pipelineParams = pipe.getPipelineParams();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",pipeId);
            jsonObject.put("name",name);
            jsonObject.put("pipelineParams",pipelineParams);

            jsonObjectList.add(jsonObject);
        });

        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,jsonObjectList);
    }

    @PostMapping("/pipeline/get")
    public UtopiaResponseModel<PipelineBean> pipelineGet(@RequestParam(value = "pipelineId",required = true) Long id){
        PipelineBean pipelineBean = pipelineService.pipelineGet(id);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,pipelineBean);
    }

    @PostMapping("/pipeline/delete")
    public UtopiaResponseModel<Void> pipelineDelete(@RequestParam(value = "pipelineId",required = true) Long id){
        pipelineService.pipelineDelete(id);
        return UtopiaResponseModel.success();
    }
}
