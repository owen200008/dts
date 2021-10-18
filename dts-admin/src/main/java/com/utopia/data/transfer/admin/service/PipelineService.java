package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.vo.req.PipelineAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelinePairAddVo;
import com.utopia.data.transfer.admin.vo.req.PipelineRegionAddVo;
import com.utopia.data.transfer.admin.vo.res.PipeDetailRes;
import com.utopia.data.transfer.admin.vo.res.PipeParamsRes;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

import java.util.List;

public interface PipelineService {

    Integer pipelineAdd(PipelineAddVo pipelineAddVo);

    PipeDetailRes pipelineDetail(Integer id);

    List<PipeParamsRes> pipelineParamsList(Integer taskId);

    void pipelinePairAdd(PipelinePairAddVo pipelinePairAddVo);


    void pipelineRegionAdd(PipelineRegionAddVo pipelineRegionAddVo);



}
