package com.utopia.data.transfer.core.code.service.impl.task.select;

import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;

import java.util.Optional;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
public interface SelectDataRule {

    boolean isStart();

    void stop();

    Optional<Message<EventDataTransaction>> selector() throws InterruptedException;

    void ack(Long id);

    void rollback();
}
