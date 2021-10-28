package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.utopia.data.transfer.admin.contants.DispatchRuleType;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.ResponseModel;
import com.utopia.data.transfer.admin.vo.req.*;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */

@RestController()
@RequestMapping(value = "/dts")
@Slf4j
public class DtsPipelineController {

    @Resource
    PipelineService pipelineService;


    @PostMapping("/pipeline/pair/add")
    public UtopiaResponseModel pipelinePairAdd(@Valid PipelinePairAddVo pipelinePairAddVo){
        pipelineService.pipelinePairAdd(pipelinePairAddVo);
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


    @PostMapping("/pipeline/add")
    public UtopiaResponseModel<JSONObject> pipelineAdd(PipelineBean pipelineBean){
        Long pi = pipelineService.pipelineAdd(pipelineBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pipelineId",pi);
        return UtopiaResponseModel.success(jsonObject);
    }
    @PostMapping("/pipeline/list")
    public UtopiaResponseModel<PageRes<List<PipelineBean>>> pipelineList(QueryPipelineVo queryPipelineVo){
        PageRes<List<PipelineBean>> page = pipelineService.pipelineList(queryPipelineVo);
        return UtopiaResponseModel.success(page);
    }

    @PostMapping("/pipeline/list/taskId")
    public UtopiaResponseModel< List<PipelineBean>> pipelineListBytaskId(@RequestParam(value = "taskId",required = true) Long id){
        List<PipelineBean> pipelineBeans = pipelineService.pipelineParamsList(id);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,pipelineBeans);
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

    @PostMapping("/pipeline/defaultParams")
    public UtopiaResponseModel<JSONObject> defaultParams(){
        String defaultParams = JSON.toJSONString(new PipelineParameter(), (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        JSONObject kafkaJson = JSONObject.parseObject(defaultParams, Feature.InitStringFieldAsEmpty);
        return UtopiaResponseModel.success(kafkaJson);
    }

    @PostMapping("/pipeline/modify")
    public UtopiaResponseModel<Void> pipelineModify(PipelineBean pipelineBean){
        pipelineService.pipelineModify(pipelineBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/pipeline/dispatchRule")
    public UtopiaResponseModel<List<String>> dispatchRule(){
        List<String> list = CollectionUtils.arrayToList(DispatchRuleType.values());
        return UtopiaResponseModel.success(list);
    }
}
