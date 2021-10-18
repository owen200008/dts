package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SourceDataMediaBeanMapper extends BaseDao {
    int deleteByExample(SourceDataMediaBeanDal example);

    int insert(SourceDataMediaBean record);

    int insertSelective(SourceDataMediaBean record);

    List<SourceDataMediaBean> selectByExample(SourceDataMediaBeanDal example);

    int updateByExampleSelective(@Param("record") SourceDataMediaBean record, @Param("example") SourceDataMediaBeanDal example);

    int updateByExample(@Param("record") SourceDataMediaBean record, @Param("example") SourceDataMediaBeanDal example);
}