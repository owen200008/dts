package com.utopia.data.transfer.admin.dao.mapper.base;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.RegionBean;
import com.utopia.data.transfer.admin.dao.entity.RegionBeanDal;
import com.utopia.data.transfer.admin.dao.mapper.RegionBeanMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegionBeanRepository extends RegionBeanMapper {


    List<RegionBean> selectByPipelineId(@Param("id") Integer pipelineId);
}