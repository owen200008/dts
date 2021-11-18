package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.utopia.data.transfer.admin.dao.entity.JarBean;
import com.utopia.data.transfer.admin.service.JarService;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.req.QueryJarVo;
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
public class DtsJarController {

    @Resource
    JarService jarService;

    @PostMapping("/jar/add")
    public UtopiaResponseModel jarAdd(@RequestParam("name")String name, @RequestParam("url")String url){
        Long jarId = jarService.jarAdd(name, url);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jarId",jarId);
        return UtopiaResponseModel.success(jsonObject);
    }

    @PostMapping("/jar/delete")
    public UtopiaResponseModel<Integer> taskDelete(@RequestParam("id")Long id){
        jarService.jarDelete(id);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/jar/list")
    public UtopiaResponseModel<PageRes<List<JarBean>>> taskList(QueryJarVo queryJarVo){
        PageRes<List<JarBean>> listPageRes = jarService.jarList(queryJarVo);
        return UtopiaResponseModel.success(listPageRes);
    }

    @PostMapping("/jar/modify")
    public UtopiaResponseModel<Void> taskModify(JarBean jarBean){
        jarService.jarModify(jarBean);
        return UtopiaResponseModel.success();
    }
}
