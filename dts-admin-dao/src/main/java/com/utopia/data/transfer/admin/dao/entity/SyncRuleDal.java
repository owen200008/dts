package com.utopia.data.transfer.admin.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class SyncRuleDal {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SyncRuleDal() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIsNull() {
            addCriterion("pipeline_id is null");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIsNotNull() {
            addCriterion("pipeline_id is not null");
            return (Criteria) this;
        }

        public Criteria andPipelineIdEqualTo(Long value) {
            addCriterion("pipeline_id =", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotEqualTo(Long value) {
            addCriterion("pipeline_id <>", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThan(Long value) {
            addCriterion("pipeline_id >", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThanOrEqualTo(Long value) {
            addCriterion("pipeline_id >=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThan(Long value) {
            addCriterion("pipeline_id <", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThanOrEqualTo(Long value) {
            addCriterion("pipeline_id <=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIn(List<Long> values) {
            addCriterion("pipeline_id in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotIn(List<Long> values) {
            addCriterion("pipeline_id not in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdBetween(Long value1, Long value2) {
            addCriterion("pipeline_id between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotBetween(Long value1, Long value2) {
            addCriterion("pipeline_id not between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeIsNull() {
            addCriterion("sync_rule_type is null");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeIsNotNull() {
            addCriterion("sync_rule_type is not null");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeEqualTo(String value) {
            addCriterion("sync_rule_type =", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeNotEqualTo(String value) {
            addCriterion("sync_rule_type <>", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeGreaterThan(String value) {
            addCriterion("sync_rule_type >", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeGreaterThanOrEqualTo(String value) {
            addCriterion("sync_rule_type >=", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeLessThan(String value) {
            addCriterion("sync_rule_type <", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeLessThanOrEqualTo(String value) {
            addCriterion("sync_rule_type <=", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeLike(String value) {
            addCriterion("sync_rule_type like", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeNotLike(String value) {
            addCriterion("sync_rule_type not like", value, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeIn(List<String> values) {
            addCriterion("sync_rule_type in", values, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeNotIn(List<String> values) {
            addCriterion("sync_rule_type not in", values, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeBetween(String value1, String value2) {
            addCriterion("sync_rule_type between", value1, value2, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeNotBetween(String value1, String value2) {
            addCriterion("sync_rule_type not between", value1, value2, "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andNamespaceIsNull() {
            addCriterion("namespace is null");
            return (Criteria) this;
        }

        public Criteria andNamespaceIsNotNull() {
            addCriterion("namespace is not null");
            return (Criteria) this;
        }

        public Criteria andNamespaceEqualTo(String value) {
            addCriterion("namespace =", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotEqualTo(String value) {
            addCriterion("namespace <>", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceGreaterThan(String value) {
            addCriterion("namespace >", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceGreaterThanOrEqualTo(String value) {
            addCriterion("namespace >=", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLessThan(String value) {
            addCriterion("namespace <", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLessThanOrEqualTo(String value) {
            addCriterion("namespace <=", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceLike(String value) {
            addCriterion("namespace like", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotLike(String value) {
            addCriterion("namespace not like", value, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceIn(List<String> values) {
            addCriterion("namespace in", values, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotIn(List<String> values) {
            addCriterion("namespace not in", values, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceBetween(String value1, String value2) {
            addCriterion("namespace between", value1, value2, "namespace");
            return (Criteria) this;
        }

        public Criteria andNamespaceNotBetween(String value1, String value2) {
            addCriterion("namespace not between", value1, value2, "namespace");
            return (Criteria) this;
        }

        public Criteria andTableIsNull() {
            addCriterion("`table` is null");
            return (Criteria) this;
        }

        public Criteria andTableIsNotNull() {
            addCriterion("`table` is not null");
            return (Criteria) this;
        }

        public Criteria andTableEqualTo(String value) {
            addCriterion("`table` =", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotEqualTo(String value) {
            addCriterion("`table` <>", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableGreaterThan(String value) {
            addCriterion("`table` >", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableGreaterThanOrEqualTo(String value) {
            addCriterion("`table` >=", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLessThan(String value) {
            addCriterion("`table` <", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLessThanOrEqualTo(String value) {
            addCriterion("`table` <=", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLike(String value) {
            addCriterion("`table` like", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotLike(String value) {
            addCriterion("`table` not like", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableIn(List<String> values) {
            addCriterion("`table` in", values, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotIn(List<String> values) {
            addCriterion("`table` not in", values, "table");
            return (Criteria) this;
        }

        public Criteria andTableBetween(String value1, String value2) {
            addCriterion("`table` between", value1, value2, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotBetween(String value1, String value2) {
            addCriterion("`table` not between", value1, value2, "table");
            return (Criteria) this;
        }

        public Criteria andStartGtidIsNull() {
            addCriterion("start_gtid is null");
            return (Criteria) this;
        }

        public Criteria andStartGtidIsNotNull() {
            addCriterion("start_gtid is not null");
            return (Criteria) this;
        }

        public Criteria andStartGtidEqualTo(String value) {
            addCriterion("start_gtid =", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidNotEqualTo(String value) {
            addCriterion("start_gtid <>", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidGreaterThan(String value) {
            addCriterion("start_gtid >", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidGreaterThanOrEqualTo(String value) {
            addCriterion("start_gtid >=", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidLessThan(String value) {
            addCriterion("start_gtid <", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidLessThanOrEqualTo(String value) {
            addCriterion("start_gtid <=", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidLike(String value) {
            addCriterion("start_gtid like", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidNotLike(String value) {
            addCriterion("start_gtid not like", value, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidIn(List<String> values) {
            addCriterion("start_gtid in", values, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidNotIn(List<String> values) {
            addCriterion("start_gtid not in", values, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidBetween(String value1, String value2) {
            addCriterion("start_gtid between", value1, value2, "startGtid");
            return (Criteria) this;
        }

        public Criteria andStartGtidNotBetween(String value1, String value2) {
            addCriterion("start_gtid not between", value1, value2, "startGtid");
            return (Criteria) this;
        }

        public Criteria andSyncRuleTypeLikeInsensitive(String value) {
            addCriterion("upper(sync_rule_type) like", value.toUpperCase(), "syncRuleType");
            return (Criteria) this;
        }

        public Criteria andNamespaceLikeInsensitive(String value) {
            addCriterion("upper(namespace) like", value.toUpperCase(), "namespace");
            return (Criteria) this;
        }

        public Criteria andTableLikeInsensitive(String value) {
            addCriterion("upper(`table`) like", value.toUpperCase(), "table");
            return (Criteria) this;
        }

        public Criteria andStartGtidLikeInsensitive(String value) {
            addCriterion("upper(start_gtid) like", value.toUpperCase(), "startGtid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}