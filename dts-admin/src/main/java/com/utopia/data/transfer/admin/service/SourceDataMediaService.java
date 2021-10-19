package com.utopia.data.transfer.admin.service;

import com.utopia.data.transfer.admin.dao.entity.SourceDataMediaBean;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryDataMediaVo;

import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
public interface SourceDataMediaService {

    SourceDataMediaBean sourceDataMediaGet(Long id);

    void sourceDataMediaDelete(Long id);

    PageRes<List<SourceDataMediaBean>> sourceDataMediaList(QueryDataMediaVo queryDataMediaVo);

    void sourceDataMediaAdd(SourceDataMediaBean sourceDataMediaBean);
}
