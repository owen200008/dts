package com.utopia.data.transfer.admin.dao.entity;

import com.utopia.data.transfer.admin.dao.base.BaseModel;
import java.io.Serializable;
import java.time.LocalDateTime;

public class PipelineBean extends BaseModel implements Serializable {
    private Long id;

    private Long taskId;

    private String name;

    private Long sourceEntityId;

    private Long targetEntityId;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    private String dispatchRuleType;

    private Boolean fullDistribute;

    private String selectParam;

    private String loadParam;

    private String pipelineParams;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(Long sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }

    public Long getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(Long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getDispatchRuleType() {
        return dispatchRuleType;
    }

    public void setDispatchRuleType(String dispatchRuleType) {
        this.dispatchRuleType = dispatchRuleType == null ? null : dispatchRuleType.trim();
    }

    public Boolean getFullDistribute() {
        return fullDistribute;
    }

    public void setFullDistribute(Boolean fullDistribute) {
        this.fullDistribute = fullDistribute;
    }

    public String getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam == null ? null : selectParam.trim();
    }

    public String getLoadParam() {
        return loadParam;
    }

    public void setLoadParam(String loadParam) {
        this.loadParam = loadParam == null ? null : loadParam.trim();
    }

    public String getPipelineParams() {
        return pipelineParams;
    }

    public void setPipelineParams(String pipelineParams) {
        this.pipelineParams = pipelineParams == null ? null : pipelineParams.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", name=").append(name);
        sb.append(", sourceEntityId=").append(sourceEntityId);
        sb.append(", targetEntityId=").append(targetEntityId);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", dispatchRuleType=").append(dispatchRuleType);
        sb.append(", fullDistribute=").append(fullDistribute);
        sb.append(", selectParam=").append(selectParam);
        sb.append(", loadParam=").append(loadParam);
        sb.append(", pipelineParams=").append(pipelineParams);
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
        PipelineBean other = (PipelineBean) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSourceEntityId() == null ? other.getSourceEntityId() == null : this.getSourceEntityId().equals(other.getSourceEntityId()))
            && (this.getTargetEntityId() == null ? other.getTargetEntityId() == null : this.getTargetEntityId().equals(other.getTargetEntityId()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDispatchRuleType() == null ? other.getDispatchRuleType() == null : this.getDispatchRuleType().equals(other.getDispatchRuleType()))
            && (this.getFullDistribute() == null ? other.getFullDistribute() == null : this.getFullDistribute().equals(other.getFullDistribute()))
            && (this.getSelectParam() == null ? other.getSelectParam() == null : this.getSelectParam().equals(other.getSelectParam()))
            && (this.getLoadParam() == null ? other.getLoadParam() == null : this.getLoadParam().equals(other.getLoadParam()))
            && (this.getPipelineParams() == null ? other.getPipelineParams() == null : this.getPipelineParams().equals(other.getPipelineParams()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSourceEntityId() == null) ? 0 : getSourceEntityId().hashCode());
        result = prime * result + ((getTargetEntityId() == null) ? 0 : getTargetEntityId().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDispatchRuleType() == null) ? 0 : getDispatchRuleType().hashCode());
        result = prime * result + ((getFullDistribute() == null) ? 0 : getFullDistribute().hashCode());
        result = prime * result + ((getSelectParam() == null) ? 0 : getSelectParam().hashCode());
        result = prime * result + ((getLoadParam() == null) ? 0 : getLoadParam().hashCode());
        result = prime * result + ((getPipelineParams() == null) ? 0 : getPipelineParams().hashCode());
        return result;
    }
}