package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.utopia.data.transfer.model.code.entity.EventColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/14
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlRunTemplate {
    private String sql;
    private List<EventColumn> columns;
}
