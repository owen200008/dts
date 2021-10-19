package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.contants.CommonUtil;
import com.utopia.data.transfer.admin.contants.ErrorCode;
import com.utopia.data.transfer.admin.dao.entity.*;
import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.dao.mapper.base.PairBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.PipelineBeanRepository;
import com.utopia.data.transfer.admin.dao.mapper.base.RegionBeanRepository;
import com.utopia.data.transfer.admin.exception.AdminException;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.admin.vo.res.EntityRes;
import com.utopia.data.transfer.admin.vo.res.PipeDetailRes;
import com.utopia.data.transfer.admin.vo.res.PipeParamsRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    PipelineBeanRepository pipelineBeanRepository;

    @Autowired
    PairBeanRepository pairBeanRepository;

    @Autowired
    RegionBeanRepository regionBeanRepository;

    @Autowired
    SourceDataMediaBeanMapper sourceDataMediaBeanMapper;

    @Autowired
    TargetDataMediaBeanMapper targetDataMediaBeanMapper;


    public static final String SOURCE_MODE = "source";
    public static final String TARGET_MODE = "target";

    @Override
    public Integer pipelineAdd(PipelineAddVo pipelineAddVo) {
        PipelineBean pipelineBean = null;
        try {
            pipelineBean = CommonUtil.snakeObjectToUnderline(pipelineAddVo, PipelineBean.class);
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
        }
        pipelineBean.setCreateTime(LocalDateTime.now());
        pipelineBean.setModifyTime(LocalDateTime.now());
        pipelineBeanMapper.insert(pipelineBean);
        return pipelineBean.getId();
    }

    @Override
    public PipeDetailRes pipelineDetail(Integer id) {

        PipeDetail pipeDetail = pipelineBeanRepository.selectDetailById(id);
        // 根据Id去获取region信息
        List<RegionBean> regionBeans = regionBeanRepository.selectByPipelineId(id);
        // 根据id获取pair列表
        List<PairDetail> pairDetails = pairBeanRepository.selectByPipelineId(id);

        if (Objects.isNull(pipeDetail)) {
            return null;
        }
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
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
        }
        return finalPipeDetailRes;

    }

    @Override
    public List<PipeParamsRes> pipelineParamsList(Integer taskId) {
        PipelineBeanDal pipelineBeanDal = new PipelineBeanDal();
        pipelineBeanDal.createCriteria().andTaskIdEqualTo(taskId);
        List<PipelineBean> pipelineBeans = pipelineBeanRepository.selectByExample(pipelineBeanDal);

        if (CollectionUtils.isEmpty(pipelineBeans)){
            return null;
        }
        List<PipeParamsRes> pipeParamsRes = new ArrayList<>();
        try {
            for (PipelineBean pipelineBean : pipelineBeans) {
                PipeParamsRes paramsRes = null;
                paramsRes = CommonUtil.snakeObjectToUnderline(pipelineBean, PipeParamsRes.class);
                pipeParamsRes.add(paramsRes);
            }
        } catch (IOException e) {
            log.error("parase object to new object fail");
            throw new AdminException(ErrorCode.PARSE_OBJECT_FAIL);
        }

        return pipeParamsRes;
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
    public List<PipeDetailRes> pipelineDetailByTaskId(Integer id) {

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
