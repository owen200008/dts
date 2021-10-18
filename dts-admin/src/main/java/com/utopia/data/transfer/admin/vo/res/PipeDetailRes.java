package com.utopia.data.transfer.admin.vo.res;

import com.utopia.data.transfer.admin.dao.entity.PairDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PipeDetailRes {


    private Integer id;

    private String name;

    private Integer sourceEntityId;

    private String sourceEntityName;

    private Integer targetEntityId;

    private String targetEntityName;

    private String sourceRegion;

    private String targetRegion;

    private Integer sourceRegionId;

    private Integer targetRegionId;

    private List<PairDetail> pairList;

}
