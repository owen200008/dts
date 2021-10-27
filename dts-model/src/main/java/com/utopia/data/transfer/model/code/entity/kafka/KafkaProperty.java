package com.utopia.data.transfer.model.code.entity.kafka;

import lombok.Data;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
@Data
public class KafkaProperty {
    /**
     *
     */
    private String serialization;

    /**
     * topic
     */
    private String topic;
}
