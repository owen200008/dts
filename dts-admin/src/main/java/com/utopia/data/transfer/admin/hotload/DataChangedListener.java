/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.utopia.data.transfer.admin.hotload;

import java.util.Objects;

/**
 * Event listener, used to send notification of event changes,
 * used to support HTTP, websocket, zookeeper and other event notifications.
 *
 * @author huangxiaofeng
 * @author xiaoyu
 */
public interface DataChangedListener {

    /**
     * invoke this method when AppAuth was received.
     * @param groupKey 配置group
     */
    void onDataChanged(String groupKey);

    /**
     * build all config
     * @return config JSON String
     */
    String buildAllConfig(String groupKey);

    default Boolean transfer2Boolean(Byte b) {
        if (Objects.isNull(b)) {
            return null;
        } else {
            return b == 1;
        }
    }
}
