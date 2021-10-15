package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TargetDataMediaBeanMapper extends BaseDao {
    int deleteByExample(TargetDataMediaBeanDal example);

    int insert(TargetDataMediaBean record);

    int insertSelective(TargetDataMediaBean record);

    List<TargetDataMediaBean> selectByExample(TargetDataMediaBeanDal example);

    int updateByExampleSelective(@Param("record") TargetDataMediaBean record, @Param("example") TargetDataMediaBeanDal example);

    int updateByExample(@Param("record") TargetDataMediaBean record, @Param("example") TargetDataMediaBeanDal example);
}