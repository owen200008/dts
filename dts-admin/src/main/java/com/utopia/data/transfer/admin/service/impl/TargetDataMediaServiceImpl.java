package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetDataMediaServiceImpl implements TargetDataMediaService {

    @Autowired
    TargetDataMediaBeanMapper targetDataMediaBeanMapper;

    @Autowired
    PairService pairService;

    @Override
    public void add(TargetDataMediaBean create) {
        targetDataMediaBeanMapper.insertSelective(create);
    }

    @Override
    public void deleteById(Long targetMediaId) {
        {
            List<PairBean> bySourceId = pairService.getByTargetId(targetMediaId);
            if (CollectionUtils.isEmpty(bySourceId)) {
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        targetDataMediaBeanMapper.deleteByPrimaryKey(targetMediaId);
    }

    @Override
    public TargetDataMediaBean getById(Long targetMediaId) {
        return targetDataMediaBeanMapper.selectByPrimaryKey(targetMediaId);
    }
}
