package com.utopia.data.transfer.model.code.bean;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
public enum StageType {
    SELECT, EXTRACT, TRANSFORM, LOAD;

    public boolean isSelect() {
        return this.equals(StageType.SELECT);
    }

    public boolean isExtract() {
        return this.equals(StageType.EXTRACT);
    }

    /**
     * transform和load一定会同时出现
     */
    public boolean isTransform() {
        return this.equals(StageType.TRANSFORM);
    }

    /**
     * transform和load一定会同时出现
     */
    public boolean isLoad() {
        return this.equals(StageType.LOAD);
    }
}
