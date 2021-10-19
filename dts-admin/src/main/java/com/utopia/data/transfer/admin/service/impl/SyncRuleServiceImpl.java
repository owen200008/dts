package com.utopia.data.transfer.admin.service.impl;

import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.service.SyncRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@Service
@Slf4j
public class SyncRuleServiceImpl  implements SyncRuleService {


    @Override
    public List<SyncRuleBean> getByPipelineId(Long pipelineId) {

        return null;
    }
}
