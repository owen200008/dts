package com.utopia.data.transfer.admin.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.dao.entity.JarBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.JarBeanMapper;
import com.utopia.data.transfer.admin.service.JarService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryJarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JarServiceImpl implements JarService {

    @Autowired
    JarBeanMapper jarBeanMapper;

    @Override
    public Long jarAdd(String name, String url) {
        JarBean jarBean = new JarBean();
        jarBean.setName(name);
        jarBean.setUrl(url);
        jarBeanMapper.insert(jarBean);
        return jarBean.getId();
    }

    @Override
    public void jarDelete(Long id) {
        jarBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageRes<List<JarBean>> jarList(QueryJarVo queryJarVo) {
        Page<Object> page = PageHelper.startPage(queryJarVo.getPageNum(), queryJarVo.getPageSize(), true);

        JarBeanDal jarBeanDal = new JarBeanDal();
        if (Objects.nonNull(queryJarVo.getName())){
            jarBeanDal.createCriteria().andNameLike(queryJarVo.getName());
        }
        List<JarBean> pairBeans = jarBeanMapper.selectByExample(jarBeanDal);
        PageRes<List<JarBean>> pageRes = PageRes.getPage(page.getTotal(), page.getPageSize(), pairBeans);
        return pageRes;
    }

    @Override
    public void jarModify(JarBean jarBean) {
        JarBeanDal jarBeanDal = new JarBeanDal();
        jarBeanDal.createCriteria().andIdEqualTo(jarBean.getId());
        jarBeanMapper.updateByExampleSelective(jarBean, jarBeanDal);
    }

    @Override
    public List<JarBean> getAll() {
        return jarBeanMapper.selectByExample(new JarBeanDal());
    }
}
