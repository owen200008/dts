package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.SyncRuleBeanMapper;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@Service
@Slf4j
public class SyncRuleServiceImpl implements SyncRuleService {

    @Autowired
    SyncRuleBeanMapper syncRuleBeanMapper;

    @Override
    public List<SyncRuleBean> getByPipelineId(Long pipelineId) {
        SyncRuleBeanDal syncRuleBeanDal = new SyncRuleBeanDal();
        syncRuleBeanDal.createCriteria().andPipelineIdEqualTo(pipelineId);
        List<SyncRuleBean> syncRuleBeans = syncRuleBeanMapper.selectByExample(syncRuleBeanDal);
        if (CollectionUtils.isEmpty(syncRuleBeans)){
            return null;
        }
        return syncRuleBeans;
    }

    @Override
    public void syncRuleAdd(SyncRuleBean syncRuleBean) {
        syncRuleBeanMapper.insert(syncRuleBean);
    }

    @Override
    public SyncRuleBean syncRuleGet(Long id) {
        SyncRuleBeanDal syncRuleBeanDal = new SyncRuleBeanDal();
        syncRuleBeanDal.createCriteria().andIdEqualTo(id);
        List<SyncRuleBean> syncRuleBeans = syncRuleBeanMapper.selectByExample(syncRuleBeanDal);
        if (CollectionUtils.isEmpty(syncRuleBeans)){
            return null;
        }
        return syncRuleBeans.get(0);
    }

    @Override
    public PageRes<List<SyncRuleBean>> syncRuleList(QuerySyncRuleVo querySyncRuleVo) {
        Page<Object> page = PageHelper.startPage(querySyncRuleVo.getPageNum(), querySyncRuleVo.getPageSize(), true);
        SyncRuleBeanDal syncRuleBeanDal = new SyncRuleBeanDal();
        syncRuleBeanDal.setOrderByClause(" create_time");
        List<SyncRuleBean> syncRuleBeans = syncRuleBeanMapper.selectByExample(syncRuleBeanDal);
        if (CollectionUtils.isEmpty(syncRuleBeans)){
            return null;
        }
        PageRes<List<SyncRuleBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), syncRuleBeans);
        return pageRes;
    }

    @Override
    public void syncRuleDelete(Long id) {
        SyncRuleBeanDal syncRuleBeanDal = new SyncRuleBeanDal();
        syncRuleBeanDal.createCriteria().andIdEqualTo(id);
        syncRuleBeanMapper.deleteByExample(syncRuleBeanDal);
    }


}
