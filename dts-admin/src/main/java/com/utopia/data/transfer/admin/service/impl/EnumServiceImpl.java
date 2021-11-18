/*
 *   Licensed to the Apache Software Foundation (final ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (final the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.utopia.data.transfer.admin.service.impl;

import com.google.common.collect.Maps;
import com.utopia.data.transfer.admin.service.DataMediaRuleTypeService;
import com.utopia.data.transfer.admin.service.EntityTypeService;
import com.utopia.data.transfer.admin.service.EnumService;
import com.utopia.data.transfer.admin.service.SyncRuleTypeService;
import com.utopia.data.transfer.admin.vo.EnumVO;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleType;
import com.utopia.data.transfer.model.code.data.media.DataMediaType;
import com.utopia.data.transfer.model.code.data.media.SyncRuleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * EnumServiceImpl.
 *
 * @author baoyuliang
 */
@Service
public class EnumServiceImpl implements EnumService {

    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SyncRuleTypeService syncRuleTypeService;
    @Autowired
    private DataMediaRuleTypeService dataMediaRuleTypeService;

    /**
     * find list of enum.
     *
     * @return {@linkplain Map}
     */
    @Override
    public Map<String, List<EnumVO>> list() {

        /**
         * commu 支持的类型
         */
        List<EnumVO> ayDataMediaType = entityTypeService.getAll().stream()
                .map(item-> new EnumVO(item.getName(), item.getName(), true))
                .collect(Collectors.toList());

        List<EnumVO> aySyncRuleType = syncRuleTypeService.getAll().stream()
                .map(httpMethodEnum -> new EnumVO(httpMethodEnum.getName(), httpMethodEnum.getName(), true))
                .collect(Collectors.toList());

        List<EnumVO> ayDataMediaRuleType = dataMediaRuleTypeService.getAll().stream()
                .map(httpMethodEnum -> new EnumVO(httpMethodEnum.getName(), httpMethodEnum.getName(), true))
                .collect(Collectors.toList());

        List<EnumVO> ayStageType = Arrays.stream(StageType.values())
                .map(httpMethodEnum -> new EnumVO(httpMethodEnum.name(), httpMethodEnum.name(), true))
                .collect(Collectors.toList());

        Map<String, List<EnumVO>> enums = Maps.newHashMap();
        enums.put("entityType", ayDataMediaType);
        enums.put("syncRule", aySyncRuleType);
        enums.put("dataMediaRule", ayDataMediaRuleType);
        enums.put("stageType", ayStageType);
        return enums;
    }
}
