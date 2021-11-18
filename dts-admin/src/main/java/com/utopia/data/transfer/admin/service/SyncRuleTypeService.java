package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleTypeVo;

import java.util.List;

public interface SyncRuleTypeService {

    /**
     *
     * @return
     */
    List<SyncRuleTypeBean> getAll();

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
    void modify(SyncRuleTypeBean jarBean);

    /**
     *
     * @param queryJarVo
     * @return
     */
    PageRes<List<SyncRuleTypeBean>> list(QuerySyncRuleTypeVo queryJarVo);
}
