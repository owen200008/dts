package com.utopia.data.transfer.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleTarget;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.pipeline.PipelineParameter;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;
import java.util.Random;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class BaseController {


    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMVC;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    public void testEntityAdd() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "1");
        params.add("type", DataMediaType.MYSQL.name());
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
        params.add("id", "1");
        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/entity/delete")
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
                MockMvcRequestBuilders.get("/dts/entity/get")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testEntityList() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "12");
        params.add("pageNum", "1");
        params.add("pageSize", "10");

        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/entity/list")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testPipelineAdd() throws Exception {
        //name
        //taskId	int	是	属于哪个任务 	111
        //sourceEntityId	int	是	从entityList中选择一个数据源 entityId	11
        //targetEntityId	int	是	从entityList中选择一个数据源 entityId	22
        //pipelineParams	String	是	通道参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "testpipelineadd");
        params.add("taskId", "1");
        params.add("sourceEntityId", "1");
        params.add("targetEntityId", "2");
        String paramss = "{\n" +
                "  \"a\":\"b\",\n" +
                "  \"c\":\"d\"\n" +
                "}";
        params.add("pipelineParams", paramss);

        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/pipeline/add")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testPipelineDetail() throws Exception {
        //name
        //taskId	int	是	属于哪个任务 	111
        //sourceEntityId	int	是	从entityList中选择一个数据源 entityId	11
        //targetEntityId	int	是	从entityList中选择一个数据源 entityId	22
        //pipelineParams	String	是	通道参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pipelineId", "1");

        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/pipeline/detail")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }


    @Test
    public void testPipelineList() throws Exception {
        //name
        //taskId	int	是	属于哪个任务 	111
        //sourceEntityId	int	是	从entityList中选择一个数据源 entityId	11
        //targetEntityId	int	是	从entityList中选择一个数据源 entityId	22
        //pipelineParams	String	是	通道参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("taskId", "11");

        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/pipeline/list")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testPipelinePairAdd() throws Exception {


    }


    @Test
    public void testPipelineRegionAdd() throws Exception {
        //pipelineId
        //通道名称	a=>b
        //sourceRegion	String	是	执行select端的dts  region	zj-ecloud
        //targetRegion	String	是	执行load端的dts  region	ln2-ecloud
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pipelineId", "11");
        params.add("sourceRegion", "ecloud");
        params.add("targetRegion", "ecloud");

        MvcResult result = this.mockMVC.perform(
                MockMvcRequestBuilders.get("/dts/pipeline/region/add")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        //CdeResponse<User> resp = JSON.parseObject(result,new TypeReference<CodeResponse<User>>(){});
        System.out.println(result.getResponse().getContentAsString());

    }


    @Test
    public void testPipelineParams(){
        PipelineParameter pipelineParameter = new PipelineParameter();

        String testParams = "{\n" +
                "  \"selectParamter\":{\n" +
                "       \"dispatchRule\":\"123\",\n" +
                "       \"dispatchRuleParam\":\"456\"\n" +
                "   },\n" +
                "  \"clientId\":22,\n" +
                "  \"batchsize\":22\n" +
                "}\n";
        PipelineParameter pipelineParameters = JSONObject.parseObject(testParams, new TypeReference<PipelineParameter>() {});

    }

}
