package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;

import java.util.List;

public interface RegionService {

    /**
     *
     * @param regionBean
     */
    Integer add(RegionBean regionBean);

    /**
     * 获取所有
     * @return
     */
    List<RegionBean> getAll();



    PageRes<List<RegionBean>> regionList(QueryRegionVo queryRegionVo);

    RegionBean regionGet(Long id);

    void pipelineDelete(Long id);

    void regionModify(RegionBean regionBean);
}
