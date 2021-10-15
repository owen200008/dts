package com.utopia.data.transfer.admin.dao.entity;

import com.utopia.data.transfer.admin.dao.base.BaseModel;
import java.io.Serializable;

public class PairBean extends BaseModel implements Serializable {
    private Integer id;

    private Integer pipelineId;

    private Integer sourceDatamediaId;

    private Integer targetDatamediaId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Integer pipelineId) {
        this.pipelineId = pipelineId;
    }

    public Integer getSourceDatamediaId() {
        return sourceDatamediaId;
    }

    public void setSourceDatamediaId(Integer sourceDatamediaId) {
        this.sourceDatamediaId = sourceDatamediaId;
    }

    public Integer getTargetDatamediaId() {
        return targetDatamediaId;
    }

    public void setTargetDatamediaId(Integer targetDatamediaId) {
        this.targetDatamediaId = targetDatamediaId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pipelineId=").append(pipelineId);
        sb.append(", sourceDatamediaId=").append(sourceDatamediaId);
        sb.append(", targetDatamediaId=").append(targetDatamediaId);
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
        PairBean other = (PairBean) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineId() == null ? other.getPipelineId() == null : this.getPipelineId().equals(other.getPipelineId()))
            && (this.getSourceDatamediaId() == null ? other.getSourceDatamediaId() == null : this.getSourceDatamediaId().equals(other.getSourceDatamediaId()))
            && (this.getTargetDatamediaId() == null ? other.getTargetDatamediaId() == null : this.getTargetDatamediaId().equals(other.getTargetDatamediaId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineId() == null) ? 0 : getPipelineId().hashCode());
        result = prime * result + ((getSourceDatamediaId() == null) ? 0 : getSourceDatamediaId().hashCode());
        result = prime * result + ((getTargetDatamediaId() == null) ? 0 : getTargetDatamediaId().hashCode());
        return result;
    }
}