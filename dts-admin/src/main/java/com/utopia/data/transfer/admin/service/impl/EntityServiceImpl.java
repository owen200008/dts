package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.dao.entity.EntityBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.EntityBeanMapper;
import com.utopia.data.transfer.admin.service.EntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
@Slf4j
public class EntityServiceImpl implements EntityService {


    @Autowired
    EntityBeanMapper entityBeanMapper;

    @Override
    public void addEntity(EntityBean entityBean) {
        entityBeanMapper.insertSelective(entityBean);
    }

    @Override
    public void deleteEntity(Long id) {
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        criteria.andIdEqualTo(id);
        entityBeanMapper.deleteByExample(entityBeanDal);
    }

    @Override
    public List<EntityBean> getAll() {
        return entityBeanMapper.selectByExample(new EntityBeanDal());
    }


}
