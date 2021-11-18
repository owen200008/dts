package com.utopia.data.transfer.admin.service;


import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryJarVo;

import java.util.Arrays;
import java.util.List;

public interface JarService {

    /**
     * 增加
     * @param name
     * @param url
     * @return
     */
    Long jarAdd(String name, String url);

    /**
     * 删除
     * @param id
     */
    void jarDelete(Long id);

    /**
     * 列表
     * @param queryJarVo
     * @return
     */
    PageRes<List<JarBean>> jarList(QueryJarVo queryJarVo);

    /**
     * modify
     * @param jarBean
     */
    void jarModify(JarBean jarBean);

    /**
     *
     * @return
     */
    List<JarBean> getAll();
}
