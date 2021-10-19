package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.dao.entity.EntityBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.EntityBeanMapper;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Long addEntity(EntityAddVo entityAddVo) {
        try {
            EntityBean entityBean = CommonUtil.snakeObjectToUnderline(entityAddVo, EntityBean.class);
            entityBean.setCreateTime(LocalDateTime.now());
            entityBean.setModifyTime(LocalDateTime.now());
            entityBeanMapper.insert(entityBean);
            return entityBean.getId();
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new UtopiaRunTimeException(ErrorCode.PARSE_OBJECT_FAIL);
        }
    }

    @Override
    public void deleteEntity(Long id) {
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        criteria.andIdEqualTo(id);
        entityBeanMapper.deleteByExample(entityBeanDal);
    }

    @Override
    public EntityBean getEntityById(Long id) {
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        criteria.andIdEqualTo(id);
        List<EntityBean> entityBeans = entityBeanMapper.selectByExample(entityBeanDal);
        if (CollectionUtils.isEmpty(entityBeans)){
            return null;
        }
        return entityBeans.get(0);
    }

    @Override
    public PageRes<List<EntityBean>> getEntityList(QueryEntityVo queryEntityVo) {
        Page<Object> page = PageHelper.startPage(queryEntityVo.getPageNum(), queryEntityVo.getPageSize(), true);
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        if (Objects.nonNull(queryEntityVo.getName())){
            criteria.andNameEqualTo(queryEntityVo.getName());
        }
        criteria.andTypeEqualTo(queryEntityVo.getType());
        List<EntityBean> entityBeans = entityBeanMapper.selectByExample(entityBeanDal);
        PageRes<List<EntityBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), entityBeans);
        return pageRes;
    }

    @Override
    public List<EntityBean> getAll() {
        return entityBeanMapper.selectByExample(new EntityBeanDal());
    }




}
