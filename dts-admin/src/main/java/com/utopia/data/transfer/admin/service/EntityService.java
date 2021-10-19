package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;
import com.utopia.data.transfer.admin.vo.res.EntityRes;

import java.util.List;

public interface EntityService {
    Integer addEntity(EntityAddVo entityAddVo);


    void deleteEntity(Integer id);


    EntityRes getEntityById(Integer id);

    PageRes<List<EntityRes>> getEntityList(QueryEntityVo queryEntityVo);

    List<EntityBean> getAll();
}
