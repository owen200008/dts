package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;
import com.utopia.data.transfer.admin.vo.req.QueryTaskVo;
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
public class DtsSourceDataMediaController {



    @Autowired
    SourceDataMediaService sourceDataMediaService;


    @PostMapping("/sourceData/add")
    public UtopiaResponseModel<JSONObject> sourceDataMediaAdd(SourceDataMediaBean sourceDataMediaBean){
        Long sourceDataMediaId = sourceDataMediaService.sourceDataMediaAdd(sourceDataMediaBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceDataId",sourceDataMediaId);
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/sourceData/get")
    public UtopiaResponseModel<SourceDataMediaBean> sourceDataMediaGet(@RequestParam(value = "sourceId")Long id){
        SourceDataMediaBean sourceDataMediaBean = sourceDataMediaService.sourceDataMediaGet(id);
        return  UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,sourceDataMediaBean);
    }

    @PostMapping("/sourceData/delete")
    public UtopiaResponseModel<Void> sourceDataMediaDelete(@RequestParam(value = "sourceId")Long id){
        sourceDataMediaService.sourceDataMediaDelete(id);
        return  UtopiaResponseModel.success();
    }

    @PostMapping("/sourceData/list")
    public UtopiaResponseModel<PageRes<List<SourceDataMediaBean>>> sourceDataMediaList(QueryDataMediaVo queryDataMediaVo){
        PageRes<List<SourceDataMediaBean>> page = sourceDataMediaService.sourceDataMediaList(queryDataMediaVo);
        return  UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,page);
    }

    @PostMapping("/sourceData/modify")
    public UtopiaResponseModel<Void> sourceDataMediaModify(SourceDataMediaBean sourceDataMediaBean){
        sourceDataMediaService.sourceDataMediaModify(sourceDataMediaBean);
        return UtopiaResponseModel.success();
    }

}
