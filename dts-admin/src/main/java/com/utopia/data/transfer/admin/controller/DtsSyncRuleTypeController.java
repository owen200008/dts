package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.service.EntityTypeService;
import com.utopia.data.transfer.admin.service.SyncRuleTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryEntityTypeVo;
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
public class DtsSyncRuleTypeController {

    @Resource
    SyncRuleTypeService syncRuleTypeService;

    @PostMapping("/syncRuleType/add")
    public UtopiaResponseModel jarAdd(@RequestParam("name")String name){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",syncRuleTypeService.add(name));
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/syncRuleType/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Integer id){
        syncRuleTypeService.delete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/syncRuleType/list")
    public UtopiaResponseModel<PageRes<List<SyncRuleTypeBean>>> taskList(QuerySyncRuleTypeVo queryJarVo){
        PageRes<List<SyncRuleTypeBean>> listPageRes = syncRuleTypeService.list(queryJarVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/syncRuleType/modify")
    public UtopiaResponseModel<Void> taskModify(SyncRuleTypeBean jarBean){
        syncRuleTypeService.modify(jarBean);
        return UtopiaResponseModel.success();
    }
}
