package com.utopia.data.transfer.admin.dao.mapper;

import com.utopia.data.transfer.admin.dao.base.BaseDao;
import com.utopia.data.transfer.admin.dao.entity.PairBean;
import com.utopia.data.transfer.admin.dao.entity.PairBeanDal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PairBeanMapper extends BaseDao {
    int deleteByExample(PairBeanDal example);

    int deleteByPrimaryKey(Long id);

    int insert(PairBean record);

    int insertSelective(PairBean record);

    List<PairBean> selectByExample(PairBeanDal example);

    int updateByExampleSelective(@Param("record") PairBean record, @Param("example") PairBeanDal example);

    int updateByExample(@Param("record") PairBean record, @Param("example") PairBeanDal example);
}