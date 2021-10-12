package com.utopia.data.transfer.model.code.pipeline;

import com.utopia.data.transfer.model.code.data.media.DataMediaPair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pipeline implements Serializable {
    private Long                id;
    private String              name;

    private List<DataMediaPair> pairs;

    private PipelineParameter params;
}
