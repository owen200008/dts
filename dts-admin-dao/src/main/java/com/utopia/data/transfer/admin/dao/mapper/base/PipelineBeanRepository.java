package com.utopia.data.transfer.admin.dao.mapper.base;

import com.utopia.data.transfer.admin.dao.mapper.PipelineBeanMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PipelineBeanRepository extends PipelineBeanMapper {


    PipeDetail selectDetailById(@Param("id") Long id);

    List<PipeDetail> selectDetailByTaskId(@Param("id")Long id);
}