package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RegionBeanMapper extends BaseDao {
    int deleteByExample(RegionBeanDal example);

    int insert(RegionBean record);

    int insertSelective(RegionBean record);

    List<RegionBean> selectByExample(RegionBeanDal example);

    int updateByExampleSelective(@Param("record") RegionBean record, @Param("example") RegionBeanDal example);

    int updateByExample(@Param("record") RegionBean record, @Param("example") RegionBeanDal example);
}