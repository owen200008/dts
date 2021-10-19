package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;
import com.utopia.data.transfer.admin.vo.req.RegionAddVo;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
public class DtsRegionController {

    @Autowired
    RegionService regionService;


    @PostMapping("/region/list")
    public UtopiaResponseModel<PageRes<List<RegionBean>>> regionList(@Valid QueryRegionVo queryRegionVo){
        PageRes<List<RegionBean>> regionBeans = regionService.regionList(queryRegionVo);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,regionBeans);
    }

    @PostMapping("/region/get")
    public UtopiaResponseModel<RegionBean> regionGet(@RequestParam(value = "regionId",required = true) Long id){
        RegionBean regionBean = regionService.regionGet(id);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,regionBean);
    }

    @PostMapping("/region/delete")
    public UtopiaResponseModel<Void> regionDelete(@RequestParam(value = "regionId",required = true) Long id){
        regionService.pipelineDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/region/all")
    public UtopiaResponseModel<List<RegionBean>> regionAll(){
        List<RegionBean> all = regionService.getAll();
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG,all);
    }



}
