package com.utopia.data.transfer.admin.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBean;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBeanDal;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.dao.mapper.EntityTypeBeanMapper;
import com.utopia.data.transfer.admin.service.EntityTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryEntityTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EntityTypeServiceImpl implements EntityTypeService {

    @Autowired
    EntityTypeBeanMapper entityTypeBeanMapper;

    @Override
    public List<EntityTypeBean> getAll() {
        return entityTypeBeanMapper.selectByExample(new EntityTypeBeanDal());
    }

    @Override
    public Integer add(String name) {
        EntityTypeBean entityTypeBean = new EntityTypeBean();
        entityTypeBean.setName(name);
        return entityTypeBeanMapper.insert(entityTypeBean);
    }

    @Override
    public void delete(Integer id) {
        entityTypeBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void modify(EntityTypeBean jarBean) {
        EntityTypeBeanDal jarBeanDal = new EntityTypeBeanDal();
        jarBeanDal.createCriteria().andIdEqualTo(jarBean.getId());
        entityTypeBeanMapper.updateByExampleSelective(jarBean, jarBeanDal);
    }

    @Override
    public PageRes<List<EntityTypeBean>> list(QueryEntityTypeVo queryJarVo) {
        Page<Object> page = PageHelper.startPage(queryJarVo.getPageNum(), queryJarVo.getPageSize(), true);

        EntityTypeBeanDal jarBeanDal = new EntityTypeBeanDal();
        if (Objects.nonNull(queryJarVo.getName())){
            jarBeanDal.createCriteria().andNameLike(queryJarVo.getName());
        }
        List<EntityTypeBean> pairBeans = entityTypeBeanMapper.selectByExample(jarBeanDal);
        PageRes<List<EntityTypeBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pairBeans);
        return pageRes;
    }
}
