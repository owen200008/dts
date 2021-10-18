package com.utopia.data.transfer.admin.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.javaws.exceptions.ErrorCodeResponseException;
import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.contants.ErrorCode;
import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.dao.entity.EntityBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.EntityBeanMapper;
import com.utopia.data.transfer.admin.exception.AdminException;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;
import com.utopia.data.transfer.admin.vo.res.EntityRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Integer addEntity(EntityAddVo entityAddVo) {
        try {
            EntityBean entityBean = CommonUtil.snakeObjectToUnderline(entityAddVo, EntityBean.class);
            entityBean.setCreateTime(LocalDateTime.now());
            entityBean.setModifyTime(LocalDateTime.now());
            entityBeanMapper.insert(entityBean);
            return entityBean.getId();
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
        }
    }

    @Override
    public void deleteEntity(Integer id) {
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        criteria.andIdEqualTo(id);
        entityBeanMapper.deleteByExample(entityBeanDal);
    }

    @Override
    public EntityRes getEntityById(Integer id) {
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        criteria.andIdEqualTo(id);
        List<EntityBean> entityBeans = entityBeanMapper.selectByExample(entityBeanDal);
        if (CollectionUtils.isEmpty(entityBeans)){
            return null;
        }
        EntityRes entityRes = null;
        try {
            entityRes = CommonUtil.snakeObjectToUnderline(entityBeans.get(0),EntityRes.class);
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
        }
        return entityRes;
    }

    @Override
    public PageRes<List<EntityRes>> getEntityList(QueryEntityVo queryEntityVo) {
        Page<Object> page = PageHelper.startPage(queryEntityVo.getPageNum(), queryEntityVo.getPageSize(), true);
        EntityBeanDal entityBeanDal = new EntityBeanDal();
        EntityBeanDal.Criteria criteria = entityBeanDal.createCriteria();
        if (Objects.nonNull(queryEntityVo.getName())){
            criteria.andNameEqualTo(queryEntityVo.getName());
        }
        criteria.andTypeEqualTo(queryEntityVo.getType());

        List<EntityBean> entityBeans = entityBeanMapper.selectByExample(entityBeanDal);

        List<EntityRes> collect = entityBeans.stream().map(e -> {
            EntityRes entityRes = null;
            try {
                entityRes = CommonUtil.snakeObjectToUnderline(e, EntityRes.class);
            } catch (IOException ioException) {
                log.error("parase object to new object fail");
                throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
            }
            return entityRes;
        }).collect(Collectors.toList());

        PageRes<List<EntityRes>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), collect);
        return pageRes;
    }





}
