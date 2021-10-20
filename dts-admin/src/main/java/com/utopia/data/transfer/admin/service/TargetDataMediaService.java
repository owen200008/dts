package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.TargetDataMediaBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
public interface TargetDataMediaService {
    TargetDataMediaBean targetDataMediaGet(Long id);

    void targetDataMediaDelete(Long id);

    PageRes<List<TargetDataMediaBean>> targetDataMediaList(QueryDataMediaVo queryDataMediaVo);

    Long targetDataMediaAdd(TargetDataMediaBean targetDataMediaBean);
}
