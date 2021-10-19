package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;

import java.util.List;

public interface PipelineService {

    /**
     * 增加pipeline
     * @param pipelineAddVo
     */
    Long pipelineAdd(PipelineAddVo pipelineAddVo);

    /**
     * 删除
     * @param pipelineId
     */
    void pipelineDelete(Long pipelineId);

    /**
     * 列表
     * @param taskId
     * @return
     */
    List<PipelineBean> pipelineParamsList(Long taskId);

    /**
     * 获取所有的pipeline
     * @return
     */
    List<PipelineBean> getAll();

    void pipelinePairAdd(PipelinePairAddVo pipelinePairAddVo);

    void pipelineRegionAdd(PipelineRegionAddVo pipelineRegionAddVo);

    PipelineBean pipelineGet(Long id);

    Long pipelineSyncRuleAdd(SyncRuleBean syncRule);

}
