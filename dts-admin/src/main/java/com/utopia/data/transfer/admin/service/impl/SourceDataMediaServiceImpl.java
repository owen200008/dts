package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SourceDataMediaServiceImpl implements SourceDataMediaService {

    @Autowired
    SourceDataMediaBeanMapper sourceDataMediaBeanMapper;

    @Autowired
    PairService pairService;

    @Override
    public void add(SourceDataMediaBean create) {
        sourceDataMediaBeanMapper.insertSelective(create);
    }

    @Override
    public void deleteById(Long sourceMediaId) {
        {
            List<PairBean> bySourceId = pairService.getBySourceId(sourceMediaId);
            if (CollectionUtils.isEmpty(bySourceId)) {
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        sourceDataMediaBeanMapper.deleteByPrimaryKey(sourceMediaId);
    }

    @Override
    public SourceDataMediaBean getById(Long sourceMediaId) {
        return sourceDataMediaBeanMapper.selectByPrimaryKey(sourceMediaId);
    }
}
