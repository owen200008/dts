package com.utopia.data.transfer.core.code.service.impl.event;

import com.utopia.module.distributed.lock.api.DtbResource;

/**
 * @author owen.cai
 * @create_date 2021/9/29
 * @alter_author
 * @alter_date
 */
public class PipelineEventContext {

    private final DtbResource dtbResource;


    public PipelineEventContext(DtbResource resource){
        this.dtbResource = resource;
    }

    public void waitResource() throws Exception {
        dtbResource.waitResource();
    }

    public void releaseResource(){
        dtbResource.releaseResource();
    }

    public boolean checkResource() {
        return dtbResource.checkResource();
    }
}
