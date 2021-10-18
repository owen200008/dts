package com.utopia.data.transfer.admin.dao.entity;

import java.util.Objects;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/18
 */
public class PairDetail {

    private String sourceName;

    private Integer sourceDatamediaId;

    private String sourceNamespace;

    private String sourceTable;

    private String targetName;

    private Integer targetDatamediaId;

    private String targetNamespace;

    private String targetTable;

    private String rule;

    public PairDetail() {
    }

    public PairDetail(String sourceName, Integer sourceDatamediaId, String sourceNamespace, String sourceTable, String targetName, Integer targetDatamediaId, String targetNamespace, String targetTable, String rule) {
        this.sourceName = sourceName;
        this.sourceDatamediaId = sourceDatamediaId;
        this.sourceNamespace = sourceNamespace;
        this.sourceTable = sourceTable;
        this.targetName = targetName;
        this.targetDatamediaId = targetDatamediaId;
        this.targetNamespace = targetNamespace;
        this.targetTable = targetTable;
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairDetail that = (PairDetail) o;
        return Objects.equals(sourceName, that.sourceName) &&
                Objects.equals(sourceDatamediaId, that.sourceDatamediaId) &&
                Objects.equals(sourceNamespace, that.sourceNamespace) &&
                Objects.equals(sourceTable, that.sourceTable) &&
                Objects.equals(targetName, that.targetName) &&
                Objects.equals(targetDatamediaId, that.targetDatamediaId) &&
                Objects.equals(targetNamespace, that.targetNamespace) &&
                Objects.equals(targetTable, that.targetTable) &&
                Objects.equals(rule, that.rule);
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceDatamediaId, sourceNamespace, sourceTable, targetDatamediaId, targetNamespace, targetTable, rule);
    }

    public Integer getSourceDatamediaId() {
        return sourceDatamediaId;
    }

    public void setSourceDatamediaId(Integer sourceDatamediaId) {
        this.sourceDatamediaId = sourceDatamediaId;
    }

    public String getSourceNamespace() {
        return sourceNamespace;
    }

    public void setSourceNamespace(String sourceNamespace) {
        this.sourceNamespace = sourceNamespace;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Integer getTargetDatamediaId() {
        return targetDatamediaId;
    }

    public void setTargetDatamediaId(Integer targetDatamediaId) {
        this.targetDatamediaId = targetDatamediaId;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
