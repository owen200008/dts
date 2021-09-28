package com.utopia.data.transfer.model.code.rsp;

import com.utopia.data.transfer.model.archetype.rsp.BaseRsp;

public class VersionRsp extends BaseRsp {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}