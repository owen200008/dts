package com.utopia.data.transfer.admin.service;


import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryPairVo;

import java.util.List;

public interface PairService {

    /**
     *
     * @param pipelineId
     * @param sourceMediaId
     * @param targetMediaId
     */
    void pairAdd(Long pipelineId, Long sourceMediaId, Long targetMediaId);

    /**
     * 删除任务
     * @param id
     */
    void pairDelete(Long id);

    /**
     * 搜索
     * @param pipelineId
     * @return
     */
    List<PairBean> getByPipelineId(Long pipelineId);

    /**
     *
     * @param sourceMediaId
     * @return
     */
    List<PairBean> getBySourceId(Long sourceMediaId);

    /**
     *
     * @param targetMediaId
     * @return
     */
    List<PairBean> getByTargetId(Long targetMediaId);

    PageRes<List<PairBean>> pairList(QueryPairVo queryPairVo);

    PairBean pairGet(Long id);
}
