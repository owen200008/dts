package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.EntityTypeBean;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryEntityTypeVo;

import java.util.List;

public interface EntityTypeService {

    /**
     *
     * @return
     */
    List<EntityTypeBean> getAll();

    /**
     *
     * @param name
     * @return
     */
    Integer add(String name);

    /**
     *
     * @param id
     */
    void delete(Integer id);

    /**
     *
     * @param jarBean
     */
    void modify(EntityTypeBean jarBean);

    /**
     *
     * @param queryJarVo
     * @return
     */
    PageRes<List<EntityTypeBean>> list(QueryEntityTypeVo queryJarVo);
}
