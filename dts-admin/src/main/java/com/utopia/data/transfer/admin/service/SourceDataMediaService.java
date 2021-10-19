package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;

public interface SourceDataMediaService {

    /**
     * 创建
     * @param create
     */
    void add(SourceDataMediaBean create);

    /**
     * delete
     * @param sourceMediaId
     */
    void deleteById(Long sourceMediaId);

    /**
     * 根据id获取
     * @param sourceMediaId
     * @return
     */
    SourceDataMediaBean getById(Long sourceMediaId);
}
