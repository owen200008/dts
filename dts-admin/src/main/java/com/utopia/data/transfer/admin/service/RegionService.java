package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;
import com.utopia.register.center.sync.InstanceResponse;

import java.util.List;
import java.util.Map;

public interface RegionService {

    /**
     *
     * @param regionBean
     */
    Long add(RegionBean regionBean);

    /**
     * 获取所有
     * @return
     */
    List<RegionBean> getAll();



    PageRes<List<RegionBean>> regionList(QueryRegionVo queryRegionVo);

    RegionBean regionGet(Long id);

    void pipelineDelete(Long id);

    void regionModify(RegionBean regionBean);

    Map<String, List<InstanceResponse>> regionNacos(String regionName);

}
