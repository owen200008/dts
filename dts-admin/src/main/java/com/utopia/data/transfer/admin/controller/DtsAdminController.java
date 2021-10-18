package com.utopia.data.transfer.admin.controller;

import com.utopia.data.transfer.admin.service.EntityService;
import com.utopia.data.transfer.admin.service.PipelineService;
import com.utopia.data.transfer.admin.service.RegionService;
import com.utopia.data.transfer.admin.service.TaskSevice;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * describe:
 *  controller
 * @author lxy
 * @date 2021/10/14
 */
@RestController
public class DtsAdminController {

    @Resource
    EntityService entityService;
    @Resource
    PipelineService pipelineService;
    @Resource
    RegionService regionService;
    @Resource
    TaskSevice taskSevice;

}
