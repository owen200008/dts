package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonObject;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.dao.entity.EntityBean;
import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.vo.EntityAddVo;
import com.utopia.data.transfer.admin.vo.PageRes;
import com.utopia.data.transfer.admin.vo.QueryEntityVo;
import com.utopia.data.transfer.admin.vo.ResponseModel;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@RestController
@RequestMapping(value = "/dts")
@Slf4j
public class DtsEntityController {


    @Resource
    EntityService entityService;

    @PostMapping("/entity/add")
    public UtopiaResponseModel<JSONObject> entityAdd(@Valid EntityAddVo entityAddVo) {
        Long entityId = entityService.addEntity(entityAddVo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("entityId", entityId);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG, jsonObject);
    }


    @PostMapping("/entity/delete")
    public ResponseModel<Void> entityDelete(@RequestParam(value = "id", required = true) Long id) {
        entityService.deleteEntity(id);
        return ResponseModel.success();
    }

    @PostMapping("/entity/get")
    public UtopiaResponseModel<EntityBean> entityGet(@RequestParam(value = "id", required = true) Long id) {
        EntityBean entityById = entityService.getEntityById(id);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG, entityById);
    }

    @PostMapping("/entity/list")
    public UtopiaResponseModel<PageRes<List<EntityBean>>> entityList(@Valid QueryEntityVo queryEntityVo) {
        PageRes<List<EntityBean>> entityById = entityService.getEntityList(queryEntityVo);
        return UtopiaResponseModel.create(UtopiaErrorCode.CODE_SUCCESS.getCode(), PathConstants.SUC_MSG, entityById);
    }

    @PostMapping("/entity/modify")
    public UtopiaResponseModel<Void> entityModify(EntityBean entityBean) {
        entityService.entityModify(entityBean);
        return UtopiaResponseModel.success();
    }

    @PostMapping("/entity/dataType")
    public UtopiaResponseModel<String> dataType(@RequestParam("type")String type) {
        String mysql = JSON.toJSONString(new MysqlProperty(), SerializerFeature.WriteMapNullValue);
        String kafka = JSON.toJSONString(new KafkaProperty(), SerializerFeature.WriteMapNullValue);

        if (type.equals(DataMediaType.MYSQL.name())){
            return  UtopiaResponseModel.success(mysql);
        }else if (type.equals(DataMediaType.KAFKA.name())){
            return  UtopiaResponseModel.success(kafka);
        }
        return  UtopiaResponseModel.success(mysql);
    }



}
