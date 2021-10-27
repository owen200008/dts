/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.utopia.data.transfer.admin.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.utopia.data.transfer.admin.listener.UtopiaDomain;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.string.UtopiaStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * The type Index controller.
 *
 * @author baoyuliang
 */
@Controller
@Slf4j
public class IndexController {

    private static volatile String version;

    private Cache<String, Integer> mapToken = CacheBuilder.newBuilder()
            .maximumSize(1024 * 1024 * 100) //最多支持100M的key
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    /**
     * Index string.
     *
     * @param model the model
     * @return the string
     */
    @RequestMapping("/index")
    public String index(final Model model) {
        model.addAttribute("domain", UtopiaDomain.getInstance().getHttpPath());
        return "index";
    }

    @RequestMapping("/ms/v1/version")
    public @ResponseBody  String getVersion() {
        if (UtopiaStringUtil.isBlank(version)) {
            ClassPathResource res = new ClassPathResource("version.txt");
            try {
                version = new BufferedReader(new InputStreamReader(res.getInputStream())).lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                log.error("getVersion error, ", e);
            }
        }
        return version;
    }

    @GetMapping("/addCheckUser")
    public @ResponseBody UtopiaResponseModel addCheckUser(String token){
        mapToken.put(token, 1);
        return UtopiaResponseModel.success();
    }

    @GetMapping("/checkValidUser")
    public @ResponseBody
    UtopiaResponseModel checkValidUser(String token){
        Integer ifPresent = mapToken.getIfPresent(token);
        if(ifPresent == null){
            return UtopiaResponseModel.fail(ErrorCode.NO_TOKEN, "no token");
        }
        return UtopiaResponseModel.success();
    }
}
