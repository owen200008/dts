package com.utopia.data.transfer.admin.dao.entity;

import com.utopia.data.transfer.admin.dao.base.BaseModel;
import java.io.Serializable;

public class SyncRule extends BaseModel implements Serializable {
    private Long id;

    private Long pipelineId;

    private String syncRuleType;

    private String namespace;

    private String table;

    private String startGtid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Long pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getSyncRuleType() {
        return syncRuleType;
    }

    public void setSyncRuleType(String syncRuleType) {
        this.syncRuleType = syncRuleType == null ? null : syncRuleType.trim();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace == null ? null : namespace.trim();
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table == null ? null : table.trim();
    }

    public String getStartGtid() {
        return startGtid;
    }

    public void setStartGtid(String startGtid) {
        this.startGtid = startGtid == null ? null : startGtid.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pipelineId=").append(pipelineId);
        sb.append(", syncRuleType=").append(syncRuleType);
        sb.append(", namespace=").append(namespace);
        sb.append(", table=").append(table);
        sb.append(", startGtid=").append(startGtid);
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
        SyncRule other = (SyncRule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineId() == null ? other.getPipelineId() == null : this.getPipelineId().equals(other.getPipelineId()))
            && (this.getSyncRuleType() == null ? other.getSyncRuleType() == null : this.getSyncRuleType().equals(other.getSyncRuleType()))
            && (this.getNamespace() == null ? other.getNamespace() == null : this.getNamespace().equals(other.getNamespace()))
            && (this.getTable() == null ? other.getTable() == null : this.getTable().equals(other.getTable()))
            && (this.getStartGtid() == null ? other.getStartGtid() == null : this.getStartGtid().equals(other.getStartGtid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineId() == null) ? 0 : getPipelineId().hashCode());
        result = prime * result + ((getSyncRuleType() == null) ? 0 : getSyncRuleType().hashCode());
        result = prime * result + ((getNamespace() == null) ? 0 : getNamespace().hashCode());
        result = prime * result + ((getTable() == null) ? 0 : getTable().hashCode());
        result = prime * result + ((getStartGtid() == null) ? 0 : getStartGtid().hashCode());
        return result;
    }
}