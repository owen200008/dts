package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaRuleTypeVo;

import java.util.List;

public interface DataMediaRuleTypeService {

    /**
     *
     * @return
     */
    List<DataMediaRuleTypeBean> getAll();


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
    void modify(DataMediaRuleTypeBean jarBean);

    /**
     *
     * @param queryJarVo
     * @return
     */
    PageRes<List<DataMediaRuleTypeBean>> list(QueryDataMediaRuleTypeVo queryJarVo);
}
