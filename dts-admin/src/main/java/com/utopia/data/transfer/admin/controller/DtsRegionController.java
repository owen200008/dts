package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.mapper.base.RegionBeanRepository;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;
import com.utopia.data.transfer.admin.vo.req.RegionAddVo;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.register.center.sync.InstanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    RegionBeanRepository regionBeanRepository;

    @PostMapping("/region/add")
    public UtopiaResponseModel<JSONObject> regionAdd(RegionBean regionBean){
        Long regionId = regionService.add(regionBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("regionId",regionId);
        return UtopiaResponseModel.success(jsonObject);
    }


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

    @PostMapping("/region/modify")
    public UtopiaResponseModel<Void> regionModify(RegionBean regionBean){
        regionService.regionModify(regionBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/region/mode")
    public UtopiaResponseModel<List<String>> modelList(){
        List<String> list = CollectionUtils.arrayToList(StageType.values());
        return  UtopiaResponseModel.success(list);
    }

    @PostMapping("/region/nacos")
    public UtopiaResponseModel<Map<String, List<InstanceResponse>>> regionNacos(@RequestParam(value = "region",required = false)String region){
        Map<String, List<InstanceResponse>> stringListMap = regionService.regionNacos(region);
        return UtopiaResponseModel.success(stringListMap);
    }

}
