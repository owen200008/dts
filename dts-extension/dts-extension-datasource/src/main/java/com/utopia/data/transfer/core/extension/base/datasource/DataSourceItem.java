package com.utopia.data.transfer.core.extension.base.datasource;

import lombok.Data;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Data
public class DataSourceItem<T> {
    private T item;

    public DataSourceItem(T t){
        this.item = t;
    }
}
