package com.utopia.data.transfer.core.code.service;

import com.utopia.data.transfer.core.code.bean.Pipeline;
import com.utopia.data.transfer.core.code.config.PipelineConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author owen.cai
 * @create_date 2021/9/27
 * @alter_author
 * @alter_date
 */
@Service
public class DTSMgr {
    @Resource
    private PipelineConfig pipelineConfig;
    
    @PostConstruct
    public void init() {
        //同步配置


        //定时获取任务



        for (Pipeline pipeline : pipelineConfig.getList()) {

        }
    }

}
