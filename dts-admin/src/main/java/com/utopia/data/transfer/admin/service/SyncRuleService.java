package com.utopia.data.transfer.admin.service;


import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;

import java.util.List;

public interface SyncRuleService {

    List<SyncRuleBean> getByPipelineId(Long pipelineId);
}
