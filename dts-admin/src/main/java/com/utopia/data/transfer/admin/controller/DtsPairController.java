package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryPairVo;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
public class DtsPairController {

    @Autowired
    PairService pairService;

    @Autowired
    SourceDataMediaService sourceDataMediaService;


    @Autowired
    TargetDataMediaService targetDataMediaService;

    @PostMapping("/pair/get/pipelineId")
    public UtopiaResponseModel<List<PairBean>> pairGetByPipelineId(@RequestParam("pipelineId")Long id){
        List<PairBean> byPipelineId = pairService.getByPipelineId(id);
        return UtopiaResponseModel.success(byPipelineId);
    }

    @PostMapping("/pair/delete")
    public UtopiaResponseModel<Void> pairDelete(@RequestParam("pairId")Long id){
        pairService.pairDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/pair/add")
    public UtopiaResponseModel<Void> pairAdd(
            @RequestParam("pipelineId")Long pipelineId,
            @RequestParam("sourceDatamediaId")Long sourceDatamediaId,
            @RequestParam("targetDatamediaId")Long targetDatamediaId
    ){
        pairService.pairAdd(pipelineId,sourceDatamediaId,targetDatamediaId);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/pair/get")
    public UtopiaResponseModel<PairBean> pairGet(@RequestParam("pairId")Long id){
        PairBean pairBean =  pairService.pairGet(id);
        return UtopiaResponseModel.success(pairBean);
    }

    @PostMapping("/pair/list")
    public UtopiaResponseModel<PageRes<List<PairBean>>> pairList(QueryPairVo queryPairVo){
        PageRes<List<PairBean>> page = pairService.pairList(queryPairVo);
        return UtopiaResponseModel.success(page);
    }

    @PostMapping("/pair/modify")
    public UtopiaResponseModel<Void> pairModify(PairBean pairBean){
        pairService.pairModify(pairBean);
        return UtopiaResponseModel.success();
    }

}
