/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utopia.data.transfer.core.code.model;

import java.io.Serializable;
import java.util.List;

/**
 * 数据对象
 * 
 * @author jianghang 2012-7-31 下午02:43:08
 */
public class Message<T> implements Serializable {

    private Long    id;
    /**
     * 数据层唯一id，保持自增用于过滤已经执行的
     */
    private List<T>         datas;

    public Message(Long id, List<T> datas){
        this.id = id;
        this.datas = datas;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

}
