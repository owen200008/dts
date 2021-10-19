package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.TaskBean;
import com.utopia.data.transfer.admin.dao.entity.TaskBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskBeanMapper extends BaseDao {
    int deleteByExample(TaskBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(TaskBean record);

    int insertSelective(TaskBean record);

    List<TaskBean> selectByExample(TaskBeanDal example);

    int updateByExampleSelective(@Param("record") TaskBean record, @Param("example") TaskBeanDal example);

    int updateByExample(@Param("record") TaskBean record, @Param("example") TaskBeanDal example);
}