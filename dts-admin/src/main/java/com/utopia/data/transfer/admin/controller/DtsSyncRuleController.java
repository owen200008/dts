package com.utopia.data.transfer.admin.controller;

import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleVo;
import com.utopia.data.transfer.model.code.data.media.SyncRuleType;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.PastOrPresent;
import java.util.Collection;
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
public class DtsSyncRuleController {

    @Autowired
    SyncRuleService syncRuleService;

    @PostMapping("/syncRule/add")
    public UtopiaResponseModel<Void> syncRuleAdd(SyncRuleBean syncRuleBean){
        syncRuleService.syncRuleAdd(syncRuleBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/syncRule/get/pipelineId")
    public UtopiaResponseModel<List<SyncRuleBean>> syncRuleGetPipelineId(@RequestParam("pipelineId")Long id){
        List<SyncRuleBean> byPipelineId = syncRuleService.getByPipelineId(id);
        return UtopiaResponseModel.success(byPipelineId);
    }

    @PostMapping("/syncRule/get")
    public UtopiaResponseModel<SyncRuleBean> syncRuleGet(@RequestParam("id")Long id){
        SyncRuleBean byPipelineId = syncRuleService.syncRuleGet(id);
        return UtopiaResponseModel.success(byPipelineId);
    }

    @PostMapping("/syncRule/list")
    public UtopiaResponseModel<PageRes<List<SyncRuleBean>>> syncRuleList(QuerySyncRuleVo querySyncRuleVo){
        PageRes<List<SyncRuleBean>> listPageRes = syncRuleService.syncRuleList(querySyncRuleVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/syncRule/delete")
    public UtopiaResponseModel<Void> syncRuleDelete(@RequestParam("id")Long id){
        syncRuleService.syncRuleDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/syncRule/modify")
    public UtopiaResponseModel<Void> syncRuleModify(SyncRuleBean syncRuleBean){
        syncRuleService.syncRuleModify(syncRuleBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/syncRule/type")
    public UtopiaResponseModel<List<String>> syncRuleType(){
        List<String> list = CollectionUtils.arrayToList(SyncRuleType.values());
        return UtopiaResponseModel.success(list);
    }

}
