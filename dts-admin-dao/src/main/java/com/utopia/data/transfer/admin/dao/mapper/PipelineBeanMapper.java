package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.PipelineBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PipelineBeanMapper extends BaseDao {
    int deleteByExample(PipelineBeanDal example);

    int insert(PipelineBean record);

    int insertSelective(PipelineBean record);

    List<PipelineBean> selectByExampleWithBLOBs(PipelineBeanDal example);

    List<PipelineBean> selectByExample(PipelineBeanDal example);

    int updateByExampleSelective(@Param("record") PipelineBean record, @Param("example") PipelineBeanDal example);

    int updateByExampleWithBLOBs(@Param("record") PipelineBean record, @Param("example") PipelineBeanDal example);

    int updateByExample(@Param("record") PipelineBean record, @Param("example") PipelineBeanDal example);
}