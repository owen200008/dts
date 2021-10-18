package com.utopia.data.transfer.admin.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class PairBeanDal {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PairBeanDal() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
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

        public Criteria andPipelineIdEqualTo(Integer value) {
            addCriterion("pipeline_id =", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotEqualTo(Integer value) {
            addCriterion("pipeline_id <>", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThan(Integer value) {
            addCriterion("pipeline_id >", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("pipeline_id >=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThan(Integer value) {
            addCriterion("pipeline_id <", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThanOrEqualTo(Integer value) {
            addCriterion("pipeline_id <=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIn(List<Integer> values) {
            addCriterion("pipeline_id in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotIn(List<Integer> values) {
            addCriterion("pipeline_id not in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdBetween(Integer value1, Integer value2) {
            addCriterion("pipeline_id between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotBetween(Integer value1, Integer value2) {
            addCriterion("pipeline_id not between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdIsNull() {
            addCriterion("source_datamedia_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdIsNotNull() {
            addCriterion("source_datamedia_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdEqualTo(Integer value) {
            addCriterion("source_datamedia_id =", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdNotEqualTo(Integer value) {
            addCriterion("source_datamedia_id <>", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdGreaterThan(Integer value) {
            addCriterion("source_datamedia_id >", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_datamedia_id >=", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdLessThan(Integer value) {
            addCriterion("source_datamedia_id <", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdLessThanOrEqualTo(Integer value) {
            addCriterion("source_datamedia_id <=", value, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdIn(List<Integer> values) {
            addCriterion("source_datamedia_id in", values, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdNotIn(List<Integer> values) {
            addCriterion("source_datamedia_id not in", values, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdBetween(Integer value1, Integer value2) {
            addCriterion("source_datamedia_id between", value1, value2, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDatamediaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("source_datamedia_id not between", value1, value2, "sourceDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdIsNull() {
            addCriterion("target_datamedia_id is null");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdIsNotNull() {
            addCriterion("target_datamedia_id is not null");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdEqualTo(Integer value) {
            addCriterion("target_datamedia_id =", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdNotEqualTo(Integer value) {
            addCriterion("target_datamedia_id <>", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdGreaterThan(Integer value) {
            addCriterion("target_datamedia_id >", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("target_datamedia_id >=", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdLessThan(Integer value) {
            addCriterion("target_datamedia_id <", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdLessThanOrEqualTo(Integer value) {
            addCriterion("target_datamedia_id <=", value, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdIn(List<Integer> values) {
            addCriterion("target_datamedia_id in", values, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdNotIn(List<Integer> values) {
            addCriterion("target_datamedia_id not in", values, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdBetween(Integer value1, Integer value2) {
            addCriterion("target_datamedia_id between", value1, value2, "targetDatamediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDatamediaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("target_datamedia_id not between", value1, value2, "targetDatamediaId");
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