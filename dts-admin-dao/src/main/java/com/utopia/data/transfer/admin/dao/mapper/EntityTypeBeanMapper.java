package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBean;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EntityTypeBeanMapper extends BaseDao {
    int deleteByExample(EntityTypeBeanDal example);

    int deleteByPrimaryKey(Integer id);

    int insert(EntityTypeBean record);

    int insertSelective(EntityTypeBean record);

    List<EntityTypeBean> selectByExample(EntityTypeBeanDal example);

    int updateByExampleSelective(@Param("record") EntityTypeBean record, @Param("example") EntityTypeBeanDal example);

    int updateByExample(@Param("record") EntityTypeBean record, @Param("example") EntityTypeBeanDal example);
}