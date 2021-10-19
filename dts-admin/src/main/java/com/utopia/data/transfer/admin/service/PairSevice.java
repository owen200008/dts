package com.utopia.data.transfer.admin.service;


import com.utopia.data.transfer.admin.dao.entity.PairBean;

import java.util.List;

public interface PairSevice {
    /**
     * 创建任务
     * @param name
     */
    void pairAdd(String name);

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
}
