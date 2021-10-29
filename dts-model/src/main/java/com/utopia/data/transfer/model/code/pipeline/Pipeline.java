package com.utopia.data.transfer.model.code.pipeline;

import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private List<DataMediaRulePair> pairs;
    private Long sourceEntityId;
    private Long targetEntityId;
    private SyncRuleTarget syncRuleTarget;

    /**
     * 如果type为kafka的话，需要通过dataType来确定数据类型
     */
    private DataMediaType dataType;

    private PipelineParameter params;

    /**
     * 任务和region的关系
     */
    private Map<StageType, String> stage            = new HashMap<>();

    /**
     * 是否关闭
     */
    private boolean             shutdown         = false;

    //cache
    private Map<Long, DataMediaRulePair> sourcePaires;

    public DataMediaRulePair getCacheSourcePairesBySourceId(long sourceId){
        if(sourcePaires == null){
            synchronized (this){
                if(sourcePaires == null){
                    sourcePaires = pairs.stream().collect(Collectors.toMap(item->item.getSource().getId(), item->item));
                }
            }
        }
        return sourcePaires.get(sourceId);
    }
}
