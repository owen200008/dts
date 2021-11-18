package com.utopia.data.transfer.admin.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PairBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.PairBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.base.PairBeanRepository;
import com.utopia.data.transfer.admin.service.PairService;

import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryPairVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class PairSeviceImpl implements PairService {

    @Autowired
    PairBeanMapper pairBeanMapper;
    @Autowired
    PairBeanRepository pairBeanRepository;
    @Override
    public void pairAdd(Long pipelineId, Long sourceMediaId, Long targetMediaId) {
        PairBean pairBean = new PairBean();
        pairBean.setPipelineId(pipelineId);
        pairBean.setSourceDatamediaId(sourceMediaId);
        pairBean.setTargetDatamediaId(targetMediaId);
        pairBeanMapper.insert(pairBean);
    }

    @Override
    public void pairDelete(Long id) {
        pairBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PairBean> getByPipelineId(Long pipelineId) {
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andPipelineIdEqualTo(pipelineId);
        return pairBeanMapper.selectByExample(pairBeanDal);
    }

    @Override
    public List<PairBean> getBySourceId(Long sourceMediaId) {
        return null;
    }

    @Override
    public List<PairBean> getByTargetId(Long targetMediaId) {
        return null;
    }

    @Override
    public PageRes<List<PairBean>> pairList(QueryPairVo queryPairVo) {
        Page<Object> page = PageHelper.startPage(queryPairVo.getPageNum(), queryPairVo.getPageSize(), true);
        PairBeanDal pairBeanDal = new PairBeanDal();
        if (Objects.nonNull(queryPairVo.getPipelineId())){
            pairBeanDal.createCriteria().andPipelineIdEqualTo(queryPairVo.getPipelineId());
        }
        pairBeanDal.setOrderByClause(" id asc");
        List<PairBean> pairBeans = pairBeanMapper.selectByExample(pairBeanDal);
        PageRes<List<PairBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pairBeans);
        return pageRes;
    }

    @Override
    public PairBean pairGet(Long id) {
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andIdEqualTo(id);
        List<PairBean> pairBeans = pairBeanMapper.selectByExample(pairBeanDal);
        if (CollectionUtils.isEmpty(pairBeans)){
            return null;
        }
        return pairBeans.get(0);
    }

    @Override
    public void pairModify(PairBean pairBean) {
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andIdEqualTo(pairBean.getId());
        pairBeanRepository.updateByExampleSelective(pairBean,pairBeanDal);
    }

    @Override
    public List<PairBean> getAll() {
        return pairBeanRepository.selectByExample(new PairBeanDal());
    }
}
