package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBeanDal;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.TargetDataMediaBeanMapper;
import com.utopia.data.transfer.admin.service.TargetDataMediaService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@Service
@Slf4j
public class TargetDataMediaServiceImpl implements TargetDataMediaService {


    @Autowired
    TargetDataMediaBeanMapper targetDataMediaBeanMapper;

    @Override
    public TargetDataMediaBean targetDataMediaGet(Long id) {
        TargetDataMediaBeanDal targetDataMediaBeanDal = new TargetDataMediaBeanDal();
        targetDataMediaBeanDal.createCriteria().andIdEqualTo(id);
        List<TargetDataMediaBean> targetDataMediaBeans = targetDataMediaBeanMapper.selectByExample(targetDataMediaBeanDal);
        if (CollectionUtils.isEmpty(targetDataMediaBeans)){
            return null;
        }
        return targetDataMediaBeans.get(0);
    }

    @Override
    public void targetDataMediaDelete(Long id) {

        TargetDataMediaBeanDal targetDataMediaBeanDal = new TargetDataMediaBeanDal();
        targetDataMediaBeanDal.createCriteria().andIdEqualTo(id);
        targetDataMediaBeanMapper.deleteByExample(targetDataMediaBeanDal);
    }

    @Override
    public PageRes<List<TargetDataMediaBean>> targetDataMediaList(QueryDataMediaVo queryDataMediaVo) {
        Page<Object> page = PageHelper.startPage(queryDataMediaVo.getPageNum(), queryDataMediaVo.getPageSize(), true);
        TargetDataMediaBeanDal targetDataMediaBeanDal = new TargetDataMediaBeanDal();
        if (StringUtils.isNotBlank(queryDataMediaVo.getName())){
            targetDataMediaBeanDal.createCriteria().andNameEqualTo(queryDataMediaVo.getName());
        }
        List<TargetDataMediaBean> targetDataMediaBeans = targetDataMediaBeanMapper.selectByExample(targetDataMediaBeanDal);
        PageRes<List<TargetDataMediaBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), targetDataMediaBeans);
        return pageRes;
    }

    @Override
    public void targetDataMediaAdd(TargetDataMediaBean targetDataMediaBean) {
        targetDataMediaBeanMapper.insert(targetDataMediaBean);
    }
}
