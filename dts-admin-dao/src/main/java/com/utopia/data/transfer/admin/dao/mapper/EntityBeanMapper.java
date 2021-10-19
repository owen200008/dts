package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.dao.entity.EntityBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EntityBeanMapper extends BaseDao {
    int deleteByExample(EntityBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(EntityBean record);

    int insertSelective(EntityBean record);

    List<EntityBean> selectByExampleWithBLOBs(EntityBeanDal example);

    List<EntityBean> selectByExample(EntityBeanDal example);

    int updateByExampleSelective(@Param("record") EntityBean record, @Param("example") EntityBeanDal example);

    int updateByExampleWithBLOBs(@Param("record") EntityBean record, @Param("example") EntityBeanDal example);

    int updateByExample(@Param("record") EntityBean record, @Param("example") EntityBeanDal example);
}