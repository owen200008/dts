package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.*;
import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.base.PairBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.RegionBeanRepository;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@Component
@Slf4j
public class PipelineServiceImpl implements PipelineService {

    @Autowired
    PipelineBeanMapper pipelineBeanMapper;

    @Autowired
    RegionService regionService;
    @Autowired
    PairService pairSevice;
    @Autowired
    SyncRuleService syncRuleService;
    @Autowired
    EntityService entityService;

    @Autowired
    PipelineBeanMapper pipelineBeanRepository;

    @Autowired
    PairBeanRepository pairBeanRepository;

    @Autowired
    RegionBeanRepository regionBeanRepository;

    @Autowired
    SourceDataMediaBeanMapper sourceDataMediaBeanMapper;

    @Autowired
    TargetDataMediaBeanMapper targetDataMediaBeanMapper;

    @Override
    public void pipelineAdd(PipelineBean pipelineBean) {
        {
            Long sourceEntityId = pipelineBean.getSourceEntityId();
            EntityBean byId = entityService.getById(sourceEntityId);
            if(Objects.isNull(byId)){
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        {
            Long targetEntityId = pipelineBean.getTargetEntityId();
            EntityBean byId = entityService.getById(targetEntityId);
            if(Objects.isNull(byId)){
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }

        pipelineBeanMapper.insert(pipelineBean);
    }

    @Override
    public void pipelineDelete(Long pipelineId) {
        //搜索下数据是否存在
        {
            List<RegionBean> byPipelineId = regionService.getByPipelineId(pipelineId);
            if(!CollectionUtils.isEmpty(byPipelineId)){
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        {
            List<PairBean> byPipelineId = pairSevice.getByPipelineId(pipelineId);
            if(!CollectionUtils.isEmpty(byPipelineId)){
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        {
            List<SyncRuleBean> byPipelineId = syncRuleService.getByPipelineId(pipelineId);
            if(!CollectionUtils.isEmpty(byPipelineId)){
                throw new UtopiaRunTimeException(ErrorCode.CHILD_NEED_DELETE_FIRST);
            }
        }
        pipelineBeanMapper.deleteByPrimaryKey(pipelineId);
    }

    @Override
    public List<PipelineBean> pipelineParamsList(Long taskId) {
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        pipelineBeanDal.createCriteria().andTaskIdEqualTo(taskId);
        return pipelineBeanRepository.selectByExample(pipelineBeanDal);
    }

    @Override
    public List<PipelineBean> getAll() {
        return pipelineBeanRepository.selectByExample(new PipelineBeanDal());
    }
}
