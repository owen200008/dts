package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class DtsTargetDataMediaController {


    @Autowired
    TargetDataMediaService targetDataMediaService;

    
    @PostMapping("/targetData/get")
    public UtopiaResponseModel<TargetDataMediaBean> targetDataMediaGet(@RequestParam(value = "targetId")Long id){
        TargetDataMediaBean targetDataMediaBean = targetDataMediaService.targetDataMediaGet(id);
        return  UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,targetDataMediaBean);
    }

    @PostMapping("/targetData/delete")
    public UtopiaResponseModel<Void> targetDataMediaDelete(@RequestParam(value = "targetId")Long id){
        targetDataMediaService.targetDataMediaDelete(id);
        return  UtopiaResponseModel.success();
    }

    @PostMapping("/targetData/list")
    public UtopiaResponseModel<PageRes<List<TargetDataMediaBean>>> targetDataMediaList(QueryDataMediaVo queryDataMediaVo){
        PageRes<List<TargetDataMediaBean>> page = targetDataMediaService.targetDataMediaList(queryDataMediaVo);
        return  UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,page);
    }

    @PostMapping("/targetData/add")
    public UtopiaResponseModel<JSONObject> targetDataMediaAdd(TargetDataMediaBean targetDataMediaBean){
        Long targetDataMediaAdd = targetDataMediaService.targetDataMediaAdd(targetDataMediaBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetDataId",targetDataMediaAdd);

        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/targetData/modify")
    public UtopiaResponseModel<Void> targetDataModify(TargetDataMediaBean targetDataMediaBean){
        targetDataMediaService.targetDataMediaModify(targetDataMediaBean);
        return UtopiaResponseModel.success();
    }
}
