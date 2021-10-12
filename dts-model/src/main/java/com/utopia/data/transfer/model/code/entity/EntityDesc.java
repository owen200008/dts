package com.utopia.data.transfer.model.code.entity;

import lombok.Data;

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
     * 描述
     */
    private String              desc;

    /**
     * 创建时间
     */
    private Date                gmtCreate;

    /**
     * 修改时间
     */
    private Date                gmtModified;


    /**
     * zk集群id，为管理方便
     */
    private Long                zkClusterId;

    /**
     * zk集群地址
     */
    private List<String>        zkClusters;

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
}
