package com.utopia.data.transfer.admin.dao.mapper.base;

import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.dao.mapper.EntityBeanMapper;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
public interface EntityBeanRepository extends EntityBeanMapper {


    void updateByUniqueKey(EntityBean entityBean);
}
