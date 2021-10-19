package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SyncRuleBeanMapper extends BaseDao {
    int deleteByExample(SyncRuleBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(SyncRuleBean record);

    int insertSelective(SyncRuleBean record);

    List<SyncRuleBean> selectByExample(SyncRuleBeanDal example);

    int updateByExampleSelective(@Param("record") SyncRuleBean record, @Param("example") SyncRuleBeanDal example);

    int updateByExample(@Param("record") SyncRuleBean record, @Param("example") SyncRuleBeanDal example);
}