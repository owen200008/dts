package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.RegionPipelineBeanMapper;
import com.utopia.data.transfer.admin.service.RegionPipelineService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionPipelineVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/27
 */
@Service
@Slf4j
public class RegionPipelineServiceImpl implements RegionPipelineService {

    @Autowired
    RegionPipelineBeanMapper regionPipelineBeanMapper;

    @Override
    public Long regionPipelineAdd(RegionPipelineBean regionPipelineBean) {
        regionPipelineBeanMapper.insertSelective(regionPipelineBean);
        return regionPipelineBean.getId();
    }

    @Override
    public void regionPipelineDelete(Long id) {
        regionPipelineBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void regionPipelineModify(RegionPipelineBean regionPipelineBean) {
        RegionPipelineBeanDal regionPipelineBeanDal = new RegionPipelineBeanDal();
        regionPipelineBeanDal.createCriteria().andIdEqualTo(regionPipelineBean.getId());
        regionPipelineBeanMapper.updateByExampleSelective(regionPipelineBean, regionPipelineBeanDal);
    }

    @Override
    public RegionPipelineBean regionPipelineGet(Long id) {
        RegionPipelineBeanDal regionPipelineBeanDal = new RegionPipelineBeanDal();
        regionPipelineBeanDal.createCriteria().andIdEqualTo(id);
        List<RegionPipelineBean> regionPipelineBeans = regionPipelineBeanMapper.selectByExample(regionPipelineBeanDal);

        if (CollectionUtils.isEmpty(regionPipelineBeans)) {
            return null;
        }
        return regionPipelineBeans.get(0);
    }

    @Override
    public PageRes<List<RegionPipelineBean>> regionPipelineList(QueryRegionPipelineVo queryRegionPipelineVo) {
        Page<Object> page = PageHelper.startPage(queryRegionPipelineVo.getPageNum(), queryRegionPipelineVo.getPageSize(), true);
        RegionPipelineBeanDal regionPipelineBeanDal = new RegionPipelineBeanDal();
        if (queryRegionPipelineVo.getPipelineId() != null) {
            regionPipelineBeanDal.createCriteria().andPipelineIdEqualTo(queryRegionPipelineVo.getPipelineId());
        }
        List<RegionPipelineBean> regionPipelineBeans = regionPipelineBeanMapper.selectByExample(regionPipelineBeanDal);
        PageRes<List<RegionPipelineBean>> page1 = PageRes.getPage(page.getTotal(), page.getPageSize(), regionPipelineBeans);
        return page1;
    }

    @Override
    public List<RegionPipelineBean> regionPipelineGetByPipelineId(Long pipelineId) {
        RegionPipelineBeanDal regionPipelineBeanDal = new RegionPipelineBeanDal();
        regionPipelineBeanDal.createCriteria().andPipelineIdEqualTo(pipelineId);

        List<RegionPipelineBean> regionPipelineBeans = regionPipelineBeanMapper.selectByExample(regionPipelineBeanDal);

        return regionPipelineBeans;
    }


}
