package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.DataMediaRuleTypeBeanMapper;
import com.utopia.data.transfer.admin.service.DataMediaRuleTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaRuleTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DataMediaRuleTypeServiceImpl implements DataMediaRuleTypeService {

    @Autowired
    DataMediaRuleTypeBeanMapper dataMediaRuleTypeBeanMapper;

    @Override
    public List<DataMediaRuleTypeBean> getAll() {
        return dataMediaRuleTypeBeanMapper.selectByExample(new DataMediaRuleTypeBeanDal());
    }

    @Override
    public Integer add(String name) {
        DataMediaRuleTypeBean entityTypeBean = new DataMediaRuleTypeBean();
        entityTypeBean.setName(name);
        return dataMediaRuleTypeBeanMapper.insert(entityTypeBean);
    }

    @Override
    public void delete(Integer id) {
        dataMediaRuleTypeBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void modify(DataMediaRuleTypeBean jarBean) {
        DataMediaRuleTypeBeanDal jarBeanDal = new DataMediaRuleTypeBeanDal();
        jarBeanDal.createCriteria().andIdEqualTo(jarBean.getId());
        dataMediaRuleTypeBeanMapper.updateByExampleSelective(jarBean, jarBeanDal);
    }

    @Override
    public PageRes<List<DataMediaRuleTypeBean>> list(QueryDataMediaRuleTypeVo queryJarVo) {
        Page<Object> page = PageHelper.startPage(queryJarVo.getPageNum(), queryJarVo.getPageSize(), true);

        DataMediaRuleTypeBeanDal jarBeanDal = new DataMediaRuleTypeBeanDal();
        if (Objects.nonNull(queryJarVo.getName())){
            jarBeanDal.createCriteria().andNameLike(queryJarVo.getName());
        }
        List<DataMediaRuleTypeBean> pairBeans = dataMediaRuleTypeBeanMapper.selectByExample(jarBeanDal);
        PageRes<List<DataMediaRuleTypeBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pairBeans);
        return pageRes;
    }
}
