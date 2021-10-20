package com.utopia.data.transfer.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.SourceDataMediaBeanMapper;
import com.utopia.data.transfer.admin.service.SourceDataMediaService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@Service
@Slf4j
public class SourceDataMediaServiceImpl  implements SourceDataMediaService {

    @Autowired
    SourceDataMediaBeanMapper sourceDataMediaBeanMapper;

    @Override
    public SourceDataMediaBean sourceDataMediaGet(Long id) {
        SourceDataMediaBeanDal sourceDataMediaBeanDal = new SourceDataMediaBeanDal();
        sourceDataMediaBeanDal.createCriteria().andIdEqualTo(id);
        List<SourceDataMediaBean> sourceDataMediaBeans = sourceDataMediaBeanMapper.selectByExample(sourceDataMediaBeanDal);
        if (CollectionUtils.isEmpty(sourceDataMediaBeans)){
            return null;
        }
        return sourceDataMediaBeans.get(0);
    }

    @Override
    public void sourceDataMediaDelete(Long id) {
        SourceDataMediaBeanDal sourceDataMediaBeanDal = new SourceDataMediaBeanDal();
        sourceDataMediaBeanDal.createCriteria().andIdEqualTo(id);
        sourceDataMediaBeanMapper.deleteByExample(sourceDataMediaBeanDal);
    }

    @Override
    public PageRes<List<SourceDataMediaBean>> sourceDataMediaList(QueryDataMediaVo queryDataMediaVo) {
        Page<Object> page = PageHelper.startPage(queryDataMediaVo.getPageNum(), queryDataMediaVo.getPageSize(), true);
        SourceDataMediaBeanDal sourceDataMediaBeanDal = new SourceDataMediaBeanDal();
        if (StringUtils.isNotBlank(queryDataMediaVo.getName())){
            sourceDataMediaBeanDal.createCriteria().andNameEqualTo(queryDataMediaVo.getName());
        }
        List<SourceDataMediaBean> sourceDataMediaBeans = sourceDataMediaBeanMapper.selectByExample(sourceDataMediaBeanDal);
        PageRes<List<SourceDataMediaBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), sourceDataMediaBeans);
        return pageRes;
    }

    @Override
    public Long sourceDataMediaAdd(SourceDataMediaBean sourceDataMediaBean) {
        sourceDataMediaBean.setCreateTime(LocalDateTime.now());
        sourceDataMediaBean.setModifyTime(LocalDateTime.now());
        sourceDataMediaBeanMapper.insert(sourceDataMediaBean);
        return sourceDataMediaBean.getId();
    }
}
