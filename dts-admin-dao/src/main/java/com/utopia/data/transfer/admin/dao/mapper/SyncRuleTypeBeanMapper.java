package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.SyncRuleTypeBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SyncRuleTypeBeanMapper extends BaseDao {
    int deleteByExample(SyncRuleTypeBeanDal example);

    int deleteByPrimaryKey(Integer id);

    int insert(SyncRuleTypeBean record);

    int insertSelective(SyncRuleTypeBean record);

    List<SyncRuleTypeBean> selectByExample(SyncRuleTypeBeanDal example);

    int updateByExampleSelective(@Param("record") SyncRuleTypeBean record, @Param("example") SyncRuleTypeBeanDal example);

    int updateByExample(@Param("record") SyncRuleTypeBean record, @Param("example") SyncRuleTypeBeanDal example);
}