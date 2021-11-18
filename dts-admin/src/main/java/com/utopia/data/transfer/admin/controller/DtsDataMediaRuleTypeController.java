package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.service.DataMediaRuleTypeService;
import com.utopia.data.transfer.admin.service.SyncRuleTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaRuleTypeVo;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleTypeVo;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
public class DtsDataMediaRuleTypeController {

    @Resource
    DataMediaRuleTypeService dataMediaRuleTypeService;

    @PostMapping("/dataMediaRuleType/add")
    public UtopiaResponseModel jarAdd(@RequestParam("name")String name){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",dataMediaRuleTypeService.add(name));
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/dataMediaRuleType/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Integer id){
        dataMediaRuleTypeService.delete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/dataMediaRuleType/list")
    public UtopiaResponseModel<PageRes<List<DataMediaRuleTypeBean>>> taskList(QueryDataMediaRuleTypeVo queryJarVo){
        PageRes<List<DataMediaRuleTypeBean>> listPageRes = dataMediaRuleTypeService.list(queryJarVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/dataMediaRuleType/modify")
    public UtopiaResponseModel<Void> taskModify(DataMediaRuleTypeBean jarBean){
        dataMediaRuleTypeService.modify(jarBean);
        return UtopiaResponseModel.success();
    }
}
