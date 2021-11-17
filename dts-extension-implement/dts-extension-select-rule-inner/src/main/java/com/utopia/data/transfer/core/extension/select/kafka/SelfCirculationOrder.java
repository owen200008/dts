package com.utopia.data.transfer.core.extension.select.kafka;

import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.utils.BooleanMutex;
import lombok.Data;

/**
 * @author owen.cai
 * @create_date 2021/11/2
 * @alter_author
 * @alter_date
 */
@Data
public class SelfCirculationOrder<TYPE, RESULT> implements SelfCirculationOperator<RESULT> {

    private boolean close = false;
    private TYPE type;
    private BooleanMutex booleanMutex = new BooleanMutex();
    private UtopiaErrorCodeClass code;
    private RESULT result;

    public void finish(UtopiaErrorCodeClass code) {
        this.code = code;
        booleanMutex.set(true);
    }

    public SelfCirculationOrder(TYPE type){
        this.type = type;
    }

    public static SelfCirculationOrder createClose(){
        SelfCirculationOrder selfCirculationOrder = new SelfCirculationOrder(null);
        selfCirculationOrder.setClose(true);
        return selfCirculationOrder;
    }
}
