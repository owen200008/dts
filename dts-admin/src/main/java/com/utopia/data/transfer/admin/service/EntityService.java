package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;

import java.util.List;

public interface EntityService {
    Long addEntity(EntityAddVo entityAddVo);


    void deleteEntity(Long id);


    EntityRes getEntityById(Long id);

    PageRes<List<EntityRes>> getEntityList(QueryEntityVo queryEntityVo);

    List<EntityBean> getAll();
}
