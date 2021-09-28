package com.utopia.data.transfer.core.code.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
public class PipelineParameter implements Serializable {
    /**
     * 目标集群
     */
    private String destinationName;

    /**
     * 订阅的客户端id
     */
    private Short clientId;

    /**
     *  订阅批次大小
     */
    private Integer batchsize          = 10000 * 10;

    /**
     * 订阅超时时间
     */
    private Long batchTimeout               = -1L;

    /**
     * 是否需要dumpSelector信息
     */
    private Boolean               dumpSelector               = true;

    /**
     * 是否需要dumpSelector的详细信息
     */
    private Boolean               dumpSelectorDetail         = true;
}
