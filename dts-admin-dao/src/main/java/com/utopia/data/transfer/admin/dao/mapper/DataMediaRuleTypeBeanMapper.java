package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBean;
import com.utopia.data.transfer.admin.dao.entity.DataMediaRuleTypeBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataMediaRuleTypeBeanMapper extends BaseDao {
    int deleteByExample(DataMediaRuleTypeBeanDal example);

    int deleteByPrimaryKey(Integer id);

    int insert(DataMediaRuleTypeBean record);

    int insertSelective(DataMediaRuleTypeBean record);

    List<DataMediaRuleTypeBean> selectByExample(DataMediaRuleTypeBeanDal example);

    int updateByExampleSelective(@Param("record") DataMediaRuleTypeBean record, @Param("example") DataMediaRuleTypeBeanDal example);

    int updateByExample(@Param("record") DataMediaRuleTypeBean record, @Param("example") DataMediaRuleTypeBeanDal example);
}