package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.RegionBeanMapper;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionVo;
import com.utopia.register.center.sync.InstanceResponse;
import com.utopia.register.center.sync.LocalCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
public class RegionServiceImpl implements RegionService, LocalCacheManager.NotifyInstance {

    @Resource
    LocalCacheManager localCacheManager;

    @Autowired
    RegionBeanMapper regionBeanMapper;

    @Override
    public Long add(RegionBean regionBean) {
        regionBeanMapper.insertSelective(regionBean);
        return regionBean.getId();
    }

    @Override
    public List<RegionBean> getAll() {
        return regionBeanMapper.selectByExample(new RegionBeanDal());
    }

    @Override
    public PageRes<List<RegionBean>> regionList(QueryRegionVo queryRegionVo) {
        Page<Object> page = PageHelper.startPage(queryRegionVo.getPageNum(), queryRegionVo.getPageSize(), true);
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        RegionBeanDal.Criteria criteria = regionBeanDal.createCriteria();
        if (queryRegionVo.getRegion() != null) {
            criteria.andRegionEqualTo(queryRegionVo.getRegion());
        }
        regionBeanDal.setOrderByClause(" id asc");
        List<RegionBean> regionBeans = regionBeanMapper.selectByExample(regionBeanDal);
        PageRes<List<RegionBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), regionBeans);
        return pageRes;
    }

    @Override
    public RegionBean regionGet(Long id) {
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(id);
        List<RegionBean> regionBeans = regionBeanMapper.selectByExample(regionBeanDal);
        if (CollectionUtils.isEmpty(regionBeans)) {
            return null;
        }
        return regionBeans.get(0);
    }

    @Override
    public void pipelineDelete(Long id) {
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(id);
        regionBeanMapper.deleteByExample(regionBeanDal);
    }

    @Override
    public void regionModify(RegionBean regionBean){
        RegionBeanDal regionBeanDal = new RegionBeanDal();
        regionBeanDal.createCriteria().andIdEqualTo(regionBean.getId());
        regionBeanMapper.updateByExampleSelective(regionBean,regionBeanDal);
    }

    @Override
    public Map<String, List<InstanceResponse>> regionNacos(String regionName) {
        if (StringUtils.isEmpty(regionName)){
            return INSTANCE_CACHE;
        }
        Map<String, List<InstanceResponse>> map = new HashMap<>();
        map.put(regionName,INSTANCE_CACHE.get(regionName));
        return map;
    }

    @PostConstruct
    public void init(){
        localCacheManager.registerNotifyInstance(PathConstants.INSTANCE_KEY,this);
    }

    public Map<String, List<InstanceResponse>> INSTANCE_CACHE = Maps.newHashMap();

    @Override
    public void notify(String key, List<InstanceResponse> o) {
        INSTANCE_CACHE.clear();
        if (PathConstants.INSTANCE_KEY.equals(key)){
            INSTANCE_CACHE = o.stream().collect(Collectors.groupingBy(instance -> instance.getMetaData().get("region")));
        }
    }
}
