package com.utopia.data.transfer.admin.dao.mapper.base;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PairBeanDal;
import com.utopia.data.transfer.admin.dao.entity.PairDetail;
import com.utopia.data.transfer.admin.dao.mapper.PairBeanMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PairBeanRepository extends PairBeanMapper {

    List<PairDetail> selectByPipelineId(@Param("id") Integer pipelineId);
}