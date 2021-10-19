package com.utopia.data.transfer.admin.service.impl;


import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.SyncRuleBeanMapper;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncRuleServiceImpl implements SyncRuleService {

    @Autowired
    SyncRuleBeanMapper syncRuleBeanMapper;

    @Override
    public List<SyncRuleBean> getByPipelineId(Long pipelineId) {
        SyncRuleBeanDal dal = new SyncRuleBeanDal();
        dal.createCriteria().andPipelineIdEqualTo(pipelineId);
        return syncRuleBeanMapper.selectByExample(dal);
    }
}
