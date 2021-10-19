package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;

public interface TargetDataMediaService {


    /**
     * 创建
     * @param create
     */
    void add(TargetDataMediaBean create);

    /**
     * delete
     * @param targetMediaId
     */
    void deleteById(Long targetMediaId);

    /**
     * 根据id获取
     * @param targetMediaId
     * @return
     */
    TargetDataMediaBean getById(Long targetMediaId);
}
