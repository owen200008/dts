package com.utopia.data.transfer.core.code.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
public class DataMediaPair implements Serializable {
    private DataMedia         source;
    private DataMedia         target;
}
