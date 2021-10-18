package com.utopia.data.transfer.model.code;

import com.utopia.data.transfer.model.code.bean.StageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 任务和region的关系
     */
    private Map<StageType, String> stage            = new HashMap<>();

    /**
     * 是否关闭
     */
    private boolean             shutdown         = false;
}
