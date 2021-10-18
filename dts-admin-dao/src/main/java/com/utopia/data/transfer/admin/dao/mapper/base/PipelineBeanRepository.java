package com.utopia.data.transfer.admin.dao.mapper.base;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.PipeDetail;
import com.utopia.data.transfer.admin.dao.entity.PipelineBean;
import com.utopia.data.transfer.admin.dao.entity.PipelineBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PipelineBeanRepository extends PipelineBeanMapper {


    PipeDetail selectDetailById(@Param("id") Integer id);
}