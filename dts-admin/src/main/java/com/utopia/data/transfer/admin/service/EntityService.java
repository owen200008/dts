package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;

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

    /**
     * 获取所有
     * @return
     */
    List<EntityBean> getAll();

    /**
     *
     * @param sourceEntityId
     * @return
     */
    EntityBean getById(Long sourceEntityId);
}
