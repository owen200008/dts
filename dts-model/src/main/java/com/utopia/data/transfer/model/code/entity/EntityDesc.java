package com.utopia.data.transfer.model.code.entity;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import lombok.Data;

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
     * 创建时间
     */
    private LocalDateTime       createTime;

    /**
     * 修改时间
     */
    private LocalDateTime       modifyTime;

    private JSONObject          params;
}
