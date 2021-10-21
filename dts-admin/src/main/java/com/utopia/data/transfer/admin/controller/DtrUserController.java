package com.utopia.data.transfer.admin.controller;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/21
 */
@RestController
@Slf4j
@RequestMapping("/dts")
public class DtrUserController {

    public final static  String NAME = "admin";
    public final static  String PASS = "admin";

    @PostMapping("/login")
    public UtopiaResponseModel userLogin(
            @RequestParam("username")String username,
            @RequestParam("password")String password
    ){
        if(StringUtils.equals(username,NAME) && StringUtils.equals(PASS,password)){
            return UtopiaResponseModel.success();
        }
        return UtopiaResponseModel.fail(ErrorCode.LOGIN_FAIL);
    }

    @PostMapping("/checkValidUser")
    public UtopiaResponseModel checkValidUser(String token){
        return UtopiaResponseModel.success();
    }
}
