package com.utopia.data.transfer.core.code.base.datasource.bean.db;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import lombok.Getter;
import lombok.Setter;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public class DbMediaSource extends DataMediaSource {
    @Getter
    @Setter
    private String              url;
    @Getter
    @Setter
    private String              username;
    @Getter
    @Setter
    private String              password;
    @Getter
    @Setter
    private String              driver;
}
