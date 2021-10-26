package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.RegionBeanMapper;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
    public Integer add(RegionBean regionBean) {
        regionBeanMapper.insertSelective(regionBean);
        return regionBean.getId();
    }

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

    @Override
    public PageRes<List<RegionBean>> regionList(QueryRegionVo queryRegionVo) {
        Page<Object> page = PageHelper.startPage(queryRegionVo.getPageNum(), queryRegionVo.getPageSize(), true);
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        RegionBeanDal.Criteria criteria = regionBeanDal.createCriteria();
        if (queryRegionVo.getRegion() != null) {
            criteria.andRegionEqualTo(queryRegionVo.getRegion());
        }
        if (queryRegionVo.getPipelineId() != null){
            criteria.andPipelineIdEqualTo(queryRegionVo.getPipelineId());
        }
        regionBeanDal.setOrderByClause(" id asc");
        List<RegionBean> regionBeans = regionBeanMapper.selectByExample(regionBeanDal);
        PageRes<List<RegionBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), regionBeans);
        return pageRes;
    }

    @Override
    public RegionBean regionGet(Long id) {
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(id.intValue());
        List<RegionBean> regionBeans = regionBeanMapper.selectByExample(regionBeanDal);
        if (CollectionUtils.isEmpty(regionBeans)) {
            return null;
        }
        return regionBeans.get(0);
    }

    @Override
    public void pipelineDelete(Long id) {
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(id.intValue());
        regionBeanMapper.deleteByExample(regionBeanDal);
    }

    @Override
    public void regionModify(RegionBean regionBean){
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(regionBean.getId());
        regionBeanMapper.updateByExampleSelective(regionBean,regionBeanDal);
    }
}
