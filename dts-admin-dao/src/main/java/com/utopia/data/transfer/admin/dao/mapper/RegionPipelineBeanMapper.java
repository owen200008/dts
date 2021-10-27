package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBean;
import com.utopia.data.transfer.admin.dao.entity.RegionPipelineBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RegionPipelineBeanMapper extends BaseDao {
    int deleteByExample(RegionPipelineBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(RegionPipelineBean record);

    int insertSelective(RegionPipelineBean record);

    List<RegionPipelineBean> selectByExample(RegionPipelineBeanDal example);

    int updateByExampleSelective(@Param("record") RegionPipelineBean record, @Param("example") RegionPipelineBeanDal example);

    int updateByExample(@Param("record") RegionPipelineBean record, @Param("example") RegionPipelineBeanDal example);
}