package com.utopia.data.transfer.admin.service;


import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleVo;

import java.util.List;

public interface SyncRuleService {

    List<SyncRuleBean> getByPipelineId(Long pipelineId);

    void syncRuleAdd(SyncRuleBean syncRuleBean);

    SyncRuleBean syncRuleGet(Long id);

    PageRes<List<SyncRuleBean>> syncRuleList(QuerySyncRuleVo querySyncRuleVo);

    void syncRuleDelete(Long id);

    void syncRuleModify(SyncRuleBean syncRuleBean);

    List<SyncRuleBean> getAll();
}
