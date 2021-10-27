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

import org.junit.Test;

import java.util.List;
import java.util.Timer;
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
}
