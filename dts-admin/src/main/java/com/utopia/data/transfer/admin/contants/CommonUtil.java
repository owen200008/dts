package com.utopia.data.transfer.admin.contants;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
@Slf4j
public class CommonUtil {

    public static <T> T snakeObjectToUnderline(Object object,Class<T> tClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String reqJson = mapper.writeValueAsString(object);
        //只赋值存在的字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T res = mapper.readValue(reqJson,tClass);
        return res;
    }



}
