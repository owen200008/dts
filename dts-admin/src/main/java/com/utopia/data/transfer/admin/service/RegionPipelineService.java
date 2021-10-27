package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryRegionPipelineVo;
import com.utopia.data.transfer.admin.vo.req.QuerySyncRuleVo;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/27
 */
public interface RegionPipelineService {

    Long regionPipelineAdd(RegionPipelineBean regionPipelineBean);

    void regionPipelineDelete(Long id);

    void regionPipelineModify(RegionPipelineBean regionPipelineBean);

    RegionPipelineBean regionPipelineGet(Long id);


    PageRes<List<RegionPipelineBean>> regionPipelineList(QueryRegionPipelineVo queryRegionPipelineVo);

    List<RegionPipelineBean> regionPipelineGetByPipelineId(Long pipelineId);

    List<RegionPipelineBean> getAll();
}
