package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.gson.JsonObject;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;

import com.utopia.register.center.sync.InstanceResponse;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/22
 */
public class ExecExample {

    public static void main(String[] args) throws InterruptedException {
        ExecTask execTask = new ExecTask();
        execTask.init();
        Timer t = new Timer();
        t.scheduleAtFixedRate(execTask, 100, 5000);


        Thread.sleep(2000);


        for (int i=0;i<111;i++){
            execTask.startTask();
            Thread.sleep(1000);
        }


        Thread.currentThread().join();

    }

    @Test
    public void test(){
        String mysql = JSON.toJSONString(new MysqlProperty(), (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        String kafka = JSON.toJSONString(new KafkaProperty(), (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        System.out.println(mysql);
        System.out.println(kafka);
        JSONObject kafkaJson = JSONObject.parseObject(kafka, Feature.InitStringFieldAsEmpty);
        JSONObject mysqlJson = JSONObject.parseObject(mysql,Feature.InitStringFieldAsEmpty);
        System.out.println(mysqlJson);
        System.out.println(kafkaJson);
    }

    @Test
    public void testInitString(){
        String defaultParams = JSON.toJSONString(new PipelineParameter(), (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        JSONObject kafkaJson = JSONObject.parseObject(defaultParams, Feature.InitStringFieldAsEmpty);
        System.out.println(defaultParams);
    }

    @Test
    public void testNocos(){
        Map<String,String> map1 = new HashMap<>();
        map1.put("region","1");


        Map<String,String> map2 = new HashMap<>();
        map2.put("region","2");
        InstanceResponse instanceResponse1 = InstanceResponse.builder()
                .ip("map11")
                .metaData(map1)
                .port(111)
                .weight(1111)
                .build();
        InstanceResponse instanceResponse2 = InstanceResponse.builder()
                .ip("map12")
                .metaData(map1)
                .port(111)
                .weight(1111)
                .build();
        InstanceResponse instanceResponse3 = InstanceResponse.builder()
                .ip("map21")
                .metaData(map2)
                .port(111)
                .weight(1111)
                .build();
        InstanceResponse instanceResponse4 = InstanceResponse.builder()
                .ip("map22")
                .metaData(map2)
                .port(111)
                .weight(1111)
                .build();
        List<InstanceResponse> list = new ArrayList<>();
        list.add(instanceResponse1);
        list.add(instanceResponse2);
        list.add(instanceResponse3);
        list.add(instanceResponse4);

        Map<String, List<InstanceResponse>> region = list.stream().collect(Collectors.groupingBy(instance -> instance.getMetaData().get("region")));
        String s = JSONObject.toJSONString(region);
        System.out.println(s);

    }
}
