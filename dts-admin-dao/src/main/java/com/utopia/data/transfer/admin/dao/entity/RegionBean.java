package com.utopia.data.transfer.admin.dao.entity;

import com.utopia.data.transfer.admin.dao.base.BaseModel;
import java.io.Serializable;

public class RegionBean extends BaseModel implements Serializable {
    private Integer id;

    private String region;

    private String mode;

    private Integer pipelineId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode == null ? null : mode.trim();
    }

    public Integer getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Integer pipelineId) {
        this.pipelineId = pipelineId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", region=").append(region);
        sb.append(", mode=").append(mode);
        sb.append(", pipelineId=").append(pipelineId);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RegionBean other = (RegionBean) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getMode() == null ? other.getMode() == null : this.getMode().equals(other.getMode()))
            && (this.getPipelineId() == null ? other.getPipelineId() == null : this.getPipelineId().equals(other.getPipelineId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getMode() == null) ? 0 : getMode().hashCode());
        result = prime * result + ((getPipelineId() == null) ? 0 : getPipelineId().hashCode());
        return result;
    }
}