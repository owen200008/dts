package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.dao.entity.JarBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JarBeanMapper extends BaseDao {
    int deleteByExample(JarBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(JarBean record);

    int insertSelective(JarBean record);

    List<JarBean> selectByExample(JarBeanDal example);

    int updateByExampleSelective(@Param("record") JarBean record, @Param("example") JarBeanDal example);

    int updateByExample(@Param("record") JarBean record, @Param("example") JarBeanDal example);
}