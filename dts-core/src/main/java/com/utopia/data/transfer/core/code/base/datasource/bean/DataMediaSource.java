package com.utopia.data.transfer.core.code.base.datasource.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Data
public class DataMediaSource implements Serializable {
    private Long                id;
    private String              name;
    private DataMediaType       type;
    /**
     * 编码方式
     */
    private String              encode;

}
