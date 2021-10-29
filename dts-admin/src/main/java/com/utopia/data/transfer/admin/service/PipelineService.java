package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.admin.vo.req.QueryPipelineVo;

import java.util.List;

public interface PipelineService {

    /**
     * 增加pipeline
     * @param pipelineBean
     */
    Long pipelineAdd(PipelineBean pipelineBean);

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


    PipelineBean pipelineGet(Long id);

    Long pipelineSyncRuleAdd(SyncRuleBean syncRule);

    PageRes<List<PipelineBean>> pipelineList(QueryPipelineVo queryPipelineVo);

    void pipelineModify(PipelineBean pipelineBean);
}
