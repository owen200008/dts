package com.utopia.data.transfer.core.code.canal;

import com.alibaba.otter.canal.sink.AbstractCanalEventDownStreamHandler;
import com.alibaba.otter.canal.store.model.Event;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Slf4j
@Data
public class CanalDownStreamHandler extends AbstractCanalEventDownStreamHandler<List<Event>> {
    private Long                     pipelineId;
    /**
     * 心跳包发送时间
     */
    private Integer                  detectingIntervalInSeconds;
}
