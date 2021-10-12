package com.utopia.data.transfer.model.code;

import com.utopia.data.transfer.model.code.bean.StageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeTask implements Serializable {
    private Long                pipelineId;
    // 任务类型
    private List<StageType>     stage            = new ArrayList();
    // 是否关闭
    private boolean             shutdown         = false;
}
