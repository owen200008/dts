package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.SyncRule;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SyncRuleMapper extends BaseDao {
    int deleteByExample(SyncRuleDal example);

    int insert(SyncRule record);

    int insertSelective(SyncRule record);

    List<SyncRule> selectByExample(SyncRuleDal example);

    int updateByExampleSelective(@Param("record") SyncRule record, @Param("example") SyncRuleDal example);

    int updateByExample(@Param("record") SyncRule record, @Param("example") SyncRuleDal example);
}