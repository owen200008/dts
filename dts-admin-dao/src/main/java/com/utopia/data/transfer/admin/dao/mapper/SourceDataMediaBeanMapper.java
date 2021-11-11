package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SourceDataMediaBeanMapper extends BaseDao {
    int deleteByExample(SourceDataMediaBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(SourceDataMediaBean record);

    int insertSelective(SourceDataMediaBean record);

    List<SourceDataMediaBean> selectByExampleWithBLOBs(SourceDataMediaBeanDal example);

    List<SourceDataMediaBean> selectByExample(SourceDataMediaBeanDal example);

    SourceDataMediaBean selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SourceDataMediaBean record, @Param("example") SourceDataMediaBeanDal example);

    int updateByExampleWithBLOBs(@Param("record") SourceDataMediaBean record, @Param("example") SourceDataMediaBeanDal example);

    int updateByExample(@Param("record") SourceDataMediaBean record, @Param("example") SourceDataMediaBeanDal example);

    int updateByPrimaryKeySelective(SourceDataMediaBean record);

    int updateByPrimaryKeyWithBLOBs(SourceDataMediaBean record);

    int updateByPrimaryKey(SourceDataMediaBean record);
}