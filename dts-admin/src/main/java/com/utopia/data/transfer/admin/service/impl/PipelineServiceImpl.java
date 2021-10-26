package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.dao.entity.*;
import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.SyncRuleBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.base.PairBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.PipelineBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.RegionBeanRepository;
import com.utopia.data.transfer.admin.service.PairService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.admin.vo.req.QueryPipelineVo;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.exception.UtopiaRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    PipelineBeanRepository pipelineBeanRepository;

    @Autowired
    PairBeanRepository pairBeanRepository;

    @Autowired
    RegionBeanRepository regionBeanRepository;

    @Autowired
    SourceDataMediaBeanMapper sourceDataMediaBeanMapper;

    @Autowired
    TargetDataMediaBeanMapper targetDataMediaBeanMapper;

    @Autowired
    SyncRuleBeanMapper syncRuleMapper;

    @Override
    public Long pipelineAdd(PipelineAddVo pipelineAddVo) {
        PipelineBean pipelineBean = null;
        try {
            pipelineBean = CommonUtil.snakeObjectToUnderline(pipelineAddVo, PipelineBean.class);
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new UtopiaRunTimeException(ErrorCode.JSON_PARSE_ERROR);
        }
        pipelineBean.setCreateTime(LocalDateTime.now());
        pipelineBean.setModifyTime(LocalDateTime.now());
        pipelineBeanMapper.insert(pipelineBean);
        return pipelineBean.getId();
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




        pipelineBeanMapper.deleteByPrimaryKey(pipelineId);
    }

    @Override
    public List<PipelineBean> pipelineParamsList(Long taskId) {
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        pipelineBeanDal.createCriteria().andTaskIdEqualTo(taskId);
        return pipelineBeanRepository.selectByExample(pipelineBeanDal);
    }

    @Override
    public void pipelinePairAdd(PipelinePairAddVo pipelinePairAddVo) {
        // 先添加source和target
        SourceDataMediaBean sourceDataMediaBean = new SourceDataMediaBean();
        sourceDataMediaBean.setName(pipelinePairAddVo.getSourceName());
        sourceDataMediaBean.setNamespace(pipelinePairAddVo.getSourceNamespace());
        sourceDataMediaBean.setTable(pipelinePairAddVo.getSourceTable());
        sourceDataMediaBean.setCreateTime(LocalDateTime.now());
        sourceDataMediaBean.setModifyTime(LocalDateTime.now());
        sourceDataMediaBeanMapper.insert(sourceDataMediaBean);

        TargetDataMediaBean targetDataMediaBean = new TargetDataMediaBean();
        targetDataMediaBean.setName(pipelinePairAddVo.getTargetName());
        targetDataMediaBean.setNamespace(pipelinePairAddVo.getTargetNamespace());
        targetDataMediaBean.setTable(pipelinePairAddVo.getTargetTable());
        targetDataMediaBean.setCreateTime(LocalDateTime.now());
        targetDataMediaBean.setModifyTime(LocalDateTime.now());

        targetDataMediaBeanMapper.insert(targetDataMediaBean);

        // 将对应id插入中间表中
        PairBean pairBean = new PairBean();
        pairBean.setPipelineId(pipelinePairAddVo.getPipelineId());
        pairBean.setSourceDatamediaId(sourceDataMediaBean.getId());
        pairBean.setTargetDatamediaId(targetDataMediaBean.getId());
        pairBeanRepository.insert(pairBean);
    }

    @Override
    public void pipelineRegionAdd(PipelineRegionAddVo pipelineRegionAddVo) {

        RegionBean sourceRegionBean = new RegionBean();
        sourceRegionBean.setMode(StageType.SELECT.toString());
        sourceRegionBean.setPipelineId(pipelineRegionAddVo.getPipelineId().longValue());
        sourceRegionBean.setRegion(pipelineRegionAddVo.getSourceRegion());

        regionBeanRepository.insert(sourceRegionBean);

        RegionBean targetRegionBean = new RegionBean();
        targetRegionBean.setMode(StageType.LOAD.toString());
        targetRegionBean.setPipelineId(pipelineRegionAddVo.getPipelineId().longValue());
        targetRegionBean.setRegion(pipelineRegionAddVo.getTargetRegion());

        regionBeanRepository.insert(targetRegionBean);
    }


    @Override
    public PipelineBean pipelineGet(Long id) {
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        pipelineBeanDal.createCriteria().andIdEqualTo(id);
        List<PipelineBean> pipelineBeans = pipelineBeanMapper.selectByExample(pipelineBeanDal);
        if (CollectionUtils.isEmpty(pipelineBeans)){
            return null;
        }
        return pipelineBeans.get(0);
    }

    @Override
    public Long pipelineSyncRuleAdd(SyncRuleBean syncRule) {
         syncRuleMapper.insert(syncRule);
         return syncRule.getId();
    }

    @Override
    public PageRes<List<PipelineBean>> pipelineList(QueryPipelineVo queryPipelineVo) {
        Page<Object> page = PageHelper.startPage(queryPipelineVo.getPageNum(), queryPipelineVo.getPageSize(), true);
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        if (queryPipelineVo.getTaskId() != null){
            pipelineBeanDal.createCriteria().andTaskIdEqualTo(queryPipelineVo.getTaskId());
        }
        pipelineBeanDal.setOrderByClause(" create_time desc");
        List<PipelineBean> pipelineBeans = pipelineBeanMapper.selectByExample(pipelineBeanDal);
        if (CollectionUtils.isEmpty(pipelineBeans)){
            return null;
        }
        PageRes<List<PipelineBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pipelineBeans);
        return pageRes;
    }

    @Override
    public void pipelineModify(PipelineBean pipelineBean) {
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        pipelineBeanDal.createCriteria().andIdEqualTo(pipelineBean.getId());
        pipelineBeanMapper.updateByExampleSelective(pipelineBean,pipelineBeanDal);
    }

    @Override
    public List<PipelineBean> getAll() {
        return pipelineBeanRepository.selectByExample(new PipelineBeanDal());
    }

}
