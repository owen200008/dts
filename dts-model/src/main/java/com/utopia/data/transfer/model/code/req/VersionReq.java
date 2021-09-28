package com.utopia.data.transfer.model.code.req;

import com.utopia.data.transfer.model.archetype.req.BaseReq;

public class VersionReq extends BaseReq {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}