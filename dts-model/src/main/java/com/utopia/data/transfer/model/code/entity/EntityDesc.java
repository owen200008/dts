package com.utopia.data.transfer.model.code.entity;

import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import lombok.Data;

import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * 编码方式
     */
    private String              encode;

    /**
     * 创建时间
     */
    private Date                gmtCreate;

    /**
     * 修改时间
     */
    private Date                gmtModified;

    /**
     * 链接到mysql的slaveId
     */
    private Long                slaveId;

    /**
     * db的配置
     */
    private String              url;
    private String              username;
    private String              password;
    private String              driver;

    private MysqlProperty       mysql;
}
