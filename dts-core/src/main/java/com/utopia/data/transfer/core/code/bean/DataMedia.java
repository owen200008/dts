package com.utopia.data.transfer.core.code.bean;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
public class DataMedia implements Serializable {

    /**
     * 唯一描述表信息
     */
    private long                id;

    /**
     * 介质源地址信息
     */
    private DataMediaSource     source;

    private String              namespace;
    private String              value;
}
