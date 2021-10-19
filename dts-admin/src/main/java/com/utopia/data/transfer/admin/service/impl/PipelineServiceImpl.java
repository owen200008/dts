package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.dao.entity.*;
import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.base.PairBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.PipelineBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.RegionBeanRepository;
import com.utopia.data.transfer.admin.service.PairSevice;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.model.archetype.ErrorCode;
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
    PairSevice pairSevice;


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

    @Override
    public void pipelineAdd(PipelineAddVo pipelineAddVo) {
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
        targetDataMediaBean.setRule(pipelinePairAddVo.getRule());
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
        sourceRegionBean.setMode(SOURCE_MODE);
        sourceRegionBean.setPipelineId(pipelineRegionAddVo.getPipelineId());
        sourceRegionBean.setRegion(pipelineRegionAddVo.getSourceRegion());

        regionBeanRepository.insert(sourceRegionBean);

        RegionBean targetRegionBean = new RegionBean();
        targetRegionBean.setMode(TARGET_MODE);
        targetRegionBean.setPipelineId(pipelineRegionAddVo.getPipelineId());
        targetRegionBean.setRegion(pipelineRegionAddVo.getTargetRegion());

        regionBeanRepository.insert(targetRegionBean);
    }




    @Override
    public List<PipeDetailRes> pipelineDetailByTaskId(Long id) {
        List<PipeDetail> pipeDetails = pipelineBeanRepository.selectDetailByTaskId(id);
        if (CollectionUtils.isEmpty(pipeDetails)) {
            return null;
        }
        List<PipeDetailRes> pipeDetailResList = new ArrayList<>();
        // 一个task只有两个pipeline可以循环获取
        for (PipeDetail pipeDetail : pipeDetails) {
            // 根据Id去获取region信息
            List<RegionBean> regionBeans = regionBeanRepository.selectByPipelineId(pipeDetail.getId());
            // 根据id获取pair列表
            List<PairDetail> pairDetails = pairBeanRepository.selectByPipelineId(pipeDetail.getId());

            PipeDetailRes finalPipeDetailRes = null;
            try {
                // 现将pipeDetail相同字段赋值
                finalPipeDetailRes = CommonUtil.snakeObjectToUnderline(pipeDetail, PipeDetailRes.class);
                finalPipeDetailRes.setPairList(pairDetails);
                // 处理region信息 变为source和target
                for (RegionBean re : regionBeans) {
                    String mode = re.getMode();
                    if (StringUtils.equals(SOURCE_MODE, mode)) {
                        finalPipeDetailRes.setSourceRegion(re.getRegion());
                        finalPipeDetailRes.setSourceRegionId(re.getId());
                    } else {
                        finalPipeDetailRes.setTargetRegion(re.getRegion());
                        finalPipeDetailRes.setTargetRegionId(re.getId());
                    }
                }
                pipeDetailResList.add(finalPipeDetailRes);
            } catch (IOException e) {
                log.error("parase object to new object fail");
                throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
            }
        }

        return pipeDetailResList;
    }

    @Override
    public List<PipelineBean> getAll() {
        return pipelineBeanRepository.selectByExample(new PipelineBeanDal());
    }

}
