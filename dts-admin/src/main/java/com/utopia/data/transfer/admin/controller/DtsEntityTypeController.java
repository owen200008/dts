package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.EntityTypeBean;
import com.utopia.data.transfer.admin.service.EntityTypeService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryEntityTypeVo;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */

@RestController()
@RequestMapping(value = "/dts/")
@Slf4j
public class DtsEntityTypeController {

    @Resource
    EntityTypeService entityTypeService;

    @PostMapping("/entityType/add")
    public UtopiaResponseModel jarAdd(@RequestParam("name")String name){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",entityTypeService.add(name));
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/entityType/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Integer id){
        entityTypeService.delete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/entityType/list")
    public UtopiaResponseModel<PageRes<List<EntityTypeBean>>> taskList(QueryEntityTypeVo queryJarVo){
        PageRes<List<EntityTypeBean>> listPageRes = entityTypeService.list(queryJarVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/entityType/modify")
    public UtopiaResponseModel<Void> taskModify(EntityTypeBean jarBean){
        entityTypeService.modify(jarBean);
        return UtopiaResponseModel.success();
    }
}
