package com.utopia.data.transfer.model.code.data.media;

import com.utopia.data.transfer.model.code.entity.EntityDesc;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public class DataMediaSource implements Serializable {
    private Long                id;
    private String              name;
    private DataMediaType       type;
    /**
     * 编码方式
     */
    private String              encode;

    /**
     * 实体信息
     */
    private EntityDesc          entityDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataMediaType getType() {
        return type;
    }

    public void setType(DataMediaType type) {
        this.type = type;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public EntityDesc getEntityDesc() {
        return entityDesc;
    }

    public void setEntityDesc(EntityDesc entityDesc) {
        this.entityDesc = entityDesc;
    }
}
