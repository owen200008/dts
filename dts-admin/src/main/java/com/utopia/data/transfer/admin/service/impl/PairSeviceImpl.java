package com.utopia.data.transfer.admin.service.impl;


import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PairBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.PairBeanMapper;
import com.utopia.data.transfer.admin.service.PairSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PairSeviceImpl implements PairSevice {

    @Autowired
    PairBeanMapper pairBeanMapper;

    @Override
    public void pairAdd(String name) {

    }

    @Override
    public void pairDelete(Long id) {

    }

    @Override
    public List<PairBean> getByPipelineId(Long pipelineId) {
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andPipelineIdEqualTo(pipelineId);
        return pairBeanMapper.selectByExample(pairBeanDal);
    }
}
