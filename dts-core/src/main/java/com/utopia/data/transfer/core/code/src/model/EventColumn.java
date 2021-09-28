package com.utopia.data.transfer.core.code.src.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Data
public class EventColumn implements Serializable {
    private int               index;

    private int               columnType;

    private String            columnName;

    /**
     * timestamp,Datetime是一个long型的数字.
     */
    private String            columnValue;

    private boolean           isNull;

    private boolean           isKey;

    /**
     * 2012.08.09 add by ljh , 新加字段，用于表明是否为真实变更字段，只针对非主键字段有效<br>
     * 因为FileResolver/EventProcessor会需要所有字段数据做分析，但又想保留按需字段同步模式
     *
     * <pre>
     * 可以简单理解isUpdate代表是否需要在目标库执行数据变更，针对update有效，默认insert/delete为true
     * 1. row模式，所有字段均为updated
     * 2. field模式，通过db反查得到的结果，均为updated
     * 3. 其余场景，根据判断是否变更过，设置updated数据
     * </pre>
     */
    private boolean           isUpdate         = true;
}
