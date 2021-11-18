package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.SyncRuleTypeBeanMapper;
import com.utopia.data.transfer.admin.service.SyncRuleTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SyncRuleTypeServiceImpl implements SyncRuleTypeService {

    @Autowired
    private SyncRuleTypeBeanMapper syncRuleTypeBeanMapper;
    /**
     *
     * @return
     */
    @Override
    public List<SyncRuleTypeBean> getAll() {
        return syncRuleTypeBeanMapper.selectByExample(new SyncRuleTypeBeanDal());
    }

    @Override
    public Integer add(String name) {
        SyncRuleTypeBean entityTypeBean = new SyncRuleTypeBean();
        entityTypeBean.setName(name);
        return syncRuleTypeBeanMapper.insert(entityTypeBean);
    }

    @Override
    public void delete(Integer id) {
        syncRuleTypeBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void modify(SyncRuleTypeBean jarBean) {
        SyncRuleTypeBeanDal jarBeanDal = new SyncRuleTypeBeanDal();
        jarBeanDal.createCriteria().andIdEqualTo(jarBean.getId());
        syncRuleTypeBeanMapper.updateByExampleSelective(jarBean, jarBeanDal);
    }

    @Override
    public PageRes<List<SyncRuleTypeBean>> list(QuerySyncRuleTypeVo queryJarVo) {
        Page<Object> page = PageHelper.startPage(queryJarVo.getPageNum(), queryJarVo.getPageSize(), true);

        SyncRuleTypeBeanDal jarBeanDal = new SyncRuleTypeBeanDal();
        if (Objects.nonNull(queryJarVo.getName())){
            jarBeanDal.createCriteria().andNameLike(queryJarVo.getName());
        }
        List<SyncRuleTypeBean> pairBeans = syncRuleTypeBeanMapper.selectByExample(jarBeanDal);
        PageRes<List<SyncRuleTypeBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pairBeans);
        return pageRes;
    }
}
