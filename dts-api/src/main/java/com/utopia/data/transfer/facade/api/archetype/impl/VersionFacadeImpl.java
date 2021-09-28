package com.utopia.data.transfer.facade.api.archetype.impl;

import com.utopia.data.transfer.facade.api.archetype.BaseFacade;
import com.utopia.data.transfer.facade.code.VersionFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取版本号
 * 代码请实现在code
 */
@Slf4j
@RestController
@RequestMapping("/ms/v1")
public class VersionFacadeImpl extends BaseFacade implements VersionFacade {

    private static volatile String version;

    @RequestMapping("/version")
    @Override
    public String getVersion() {
        if (StringUtils.isBlank(version)) {
            ClassPathResource res = new ClassPathResource("version.txt");
            try {
                version = new BufferedReader(new InputStreamReader(res.getInputStream())).lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("getVersion error, ", e);
            }
        }
        return version;
    }
}