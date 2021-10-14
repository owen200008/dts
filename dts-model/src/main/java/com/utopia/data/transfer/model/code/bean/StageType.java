package com.utopia.data.transfer.model.code.bean;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
public enum StageType {
    SELECT, LOAD;

    public boolean isSelect() {
        return this.equals(StageType.SELECT);
    }

    /**
     * transform和load一定会同时出现
     */
    public boolean isLoad() {
        return this.equals(StageType.LOAD);
    }
}
