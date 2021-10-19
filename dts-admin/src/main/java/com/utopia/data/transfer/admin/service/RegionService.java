package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.RegionBean;

import java.util.List;

public interface RegionService {

    /**
     *
     * @param regionBean
     */
    void add(RegionBean regionBean);

    /**
     * 获取所有
     * @return
     */
    List<RegionBean> getAll();

    /**
     * 根据pipelineid搜索
     * @param pipelineId
     * @return
     */
    List<RegionBean> getByPipelineId(Long pipelineId);
}
