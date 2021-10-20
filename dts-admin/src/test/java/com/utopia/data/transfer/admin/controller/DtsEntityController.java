package com.utopia.data.transfer.admin.controller;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/20
 */
public class DtsEntityController extends BaseController{


    @Test
    public void testEntityAdd() throws  Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "1");
        params.add("type","12" );
        params.add("username", "1");
        params.add("url", "1");
        params.add("password","1" );
        params.add("slaveId", String.valueOf(RandomUtils.nextInt()));
        params.add("driver","1" );
        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.post("/dts/entity/add")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CodeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});

        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testEntityDelete() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "3");
        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.post("/dts/entity/delete")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }
    @Test
    public void testEntityGet() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "3");
        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.post("/dts/entity/get")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }




}
