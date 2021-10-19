package com.utopia.data.transfer.admin.service.impl;


import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PairBeanDal;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.dao.mapper.PairBeanMapper;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PairSeviceImpl implements PairService {

    @Autowired
    PairBeanMapper pairBeanMapper;

    @Autowired
    SourceDataMediaService sourceDataMediaService;
    @Autowired
    TargetDataMediaService targetDataMediaService;

    @Override
    public void pairAdd(Long pipelineId, Long sourceMediaId, Long targetMediaId) {
        //确认media存在
        {
            SourceDataMediaBean byId = sourceDataMediaService.getById(sourceMediaId);
            if(byId == null){
                throw new UtopiaRunTimeException(ErrorCode.SOURCE_DATAMEDIA_NO_FIND);
            }
        }
        {
            TargetDataMediaBean byId = targetDataMediaService.getById(targetMediaId);
            if(byId == null){
                throw new UtopiaRunTimeException(ErrorCode.TARGET_DATAMEDIA_NO_FIND);
            }
        }

        PairBean pairBean = new PairBean();
        pairBean.setPipelineId(pipelineId);
        pairBean.setSourceDatamediaId(sourceMediaId);
        pairBean.setTargetDatamediaId(targetMediaId);
        pairBeanMapper.insertSelective(pairBean);
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
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andSourceDatamediaIdEqualTo(sourceMediaId);
        return pairBeanMapper.selectByExample(pairBeanDal);
    }

    @Override
    public List<PairBean> getByTargetId(Long targetMediaId) {
        PairBeanDal pairBeanDal = new PairBeanDal();
        pairBeanDal.createCriteria().andTargetDatamediaIdEqualTo(targetMediaId);
        return pairBeanMapper.selectByExample(pairBeanDal);
    }
}
