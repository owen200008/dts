package com.utopia.data.transfer.model.code.entity;

import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import lombok.Data;

import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Data
public class EntityDesc implements Serializable {
    private Long                id;
    /**
     * 对应的名字
     */
    private String              name;

    /**
     * 实体类型
     */
    private DataMediaType       type;
    /**
     * 如果type为kafka的话，需要通过dataType来确定数据类型
     */
    private DataMediaType       dataType;

    /**
     * 编码方式
     */
    private String              encode;

    /**
     * 创建时间
     */
    private LocalDateTime       createTime;

    /**
     * 修改时间
     */
    private LocalDateTime       modifyTime;

    /**
     * db的配置
     */
    private String              url;
    private String              username;
    private String              password;
    private String              driver;

    private MysqlProperty       mysql;

    private KafkaProperty       kafka;
}
