package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.service.RegionPipelineService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionPipelineVo;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleVo;
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
 * @date 2021/10/27
 */
@RestController()
@RequestMapping(value = "/dts/")
@Slf4j
public class DtsRegionPipelineController {


    @Autowired
    RegionPipelineService regionPipelineService;

    @PostMapping("/reg_pipe/add")
    public UtopiaResponseModel<JSONObject> regionPipelineAdd(RegionPipelineBean regionPipelineBean){
        Long regionId = regionPipelineService.regionPipelineAdd(regionPipelineBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",regionId);
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/reg_pipe/modify")
    public UtopiaResponseModel<Void> regionPipelineModify(RegionPipelineBean regionPipelineBean){
        regionPipelineService.regionPipelineModify(regionPipelineBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/reg_pipe/get")
    public UtopiaResponseModel<RegionPipelineBean> regionPipelineGet(@RequestParam("id")Long id){
        RegionPipelineBean regionPipelineBean = regionPipelineService.regionPipelineGet(id);
        return UtopiaResponseModel.success(regionPipelineBean);
    }

    @PostMapping("/reg_pipe/list")
    public UtopiaResponseModel<PageRes<List<RegionPipelineBean>>> regionPipelineList(QueryRegionPipelineVo queryRegionPipelineVo){
        PageRes<List<RegionPipelineBean>> listPageRes = regionPipelineService.regionPipelineList(queryRegionPipelineVo);
        return UtopiaResponseModel.success(listPageRes);
    }
    @PostMapping("/reg_pipe/delete")
    public UtopiaResponseModel<Void> regionPipelineDelete(@RequestParam("id")Long id){
        regionPipelineService.regionPipelineDelete(id);
        return UtopiaResponseModel.success();
    }

}
