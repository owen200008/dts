package com.utopia.data.transfer.model.code.bean;

import com.utopia.string.UtopiaStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
public enum StageType {
    SELECT, LOAD;

    public interface GetRegionCallback{
        String getRegion(StageType type);
    }
    public static Map<StageType, String> checkAndChange(GetRegionCallback callback) {
        StageType[] values = StageType.values();
        Map<StageType, String> ret = new HashMap();
        for (StageType value : values) {
            String region = callback.getRegion(value);
            if(UtopiaStringUtil.isBlank(region)){
                return null;
            }
            ret.put(value, region);
        }
        return ret;
    }

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
