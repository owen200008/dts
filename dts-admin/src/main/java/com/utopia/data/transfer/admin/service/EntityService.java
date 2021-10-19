package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;

import java.util.List;

public interface EntityService {
    /**
     * 添加
     * @param entityBean
     * @return
     */
    void addEntity(EntityBean entityBean);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteEntity(Long id);


    EntityBean getEntityById(Long id);

    PageRes<List<EntityBean>> getEntityList(QueryEntityVo queryEntityVo);

    List<EntityBean> getAll();

    /**
     *
     * @param sourceEntityId
     * @return
     */
    EntityBean getById(Long sourceEntityId);
}
