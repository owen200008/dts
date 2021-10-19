package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.RegionBeanMapper;
import com.utopia.data.transfer.admin.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
public class RegionServiceImpl implements RegionService {
    @Autowired
    RegionBeanMapper regionBeanMapper;

    @Override
    public List<RegionBean> getAll() {
        return regionBeanMapper.selectByExample(new RegionBeanDal());
    }

    @Override
    public List<RegionBean> getByPipelineId(Long pipelineId) {
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andPipelineIdEqualTo(pipelineId);
        return regionBeanMapper.selectByExample(regionBeanDal);
    }
}
