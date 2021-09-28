package com.utopia.data.transfer.core.code.base.datasource;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataSourceItem;
import com.utopia.extension.UtopiaSPI;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface DataSourceHandler {

    /**
     * 扩展功能,可以自定义一些自己实现的 {@link DataSource} <br/>
     * 如果返回的 {@link DataSourceService} 不为空，则 {@link DataSourceService} 会采用此 {@link DataSourceService}
     *
     * @return
     */
    DataSourceItem create(DataMediaSource dbMediaSource);

    /**
     * 扩展功能,可以在 {@link DataSource} 被 destroy 之前做一些事情<br/>
     * 如果返回 <code>true</code>，则暗示此 dataSource 不会被后续流程 destroy. 通常 filter 可以自己 destroy 自己在 preFilter 产生的 dataSource.
     *
     * @return
     */
    void destory(DataSourceItem dataSource) throws SQLException;
}
