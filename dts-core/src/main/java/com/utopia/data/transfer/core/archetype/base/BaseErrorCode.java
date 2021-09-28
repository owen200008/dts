package com.utopia.data.transfer.core.archetype.base;

import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.model.rsp.UtopiaErrorCode;

/**
 * Created by czq on 2019/6/27.
 */
public class BaseErrorCode extends UtopiaErrorCode {

    public final static UtopiaErrorCodeClass REQUEST_PARAM_INVALID = new UtopiaErrorCodeClass(24001 + 1, "param invalid");
    public final static UtopiaErrorCodeClass REQUEST_PARAM_MISS = new UtopiaErrorCodeClass(24001 + 2, "param miss");
    public final static UtopiaErrorCodeClass SYSTEM_ERROR = new UtopiaErrorCodeClass(24001 + 3, "system error");
    public final static UtopiaErrorCodeClass DB_ERROR = new UtopiaErrorCodeClass(24001 + 4, "db error");

    public static UtopiaResponseModel<String> createResponse(UtopiaErrorCodeClass utopiaErrorCodeClass, StringBuilder stringBuilder){
        UtopiaResponseModel<String> em = new UtopiaResponseModel<>(utopiaErrorCodeClass);
        if(stringBuilder != null){
            if (stringBuilder.toString().length() > 0) {
                em.setMsg(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }
        }
        return em;
    }
    public static UtopiaResponseModel<String> createResponse(UtopiaErrorCodeClass utopiaErrorCodeClass, String stringBuilder){
        UtopiaResponseModel<String> em = new UtopiaResponseModel<>(utopiaErrorCodeClass);
        if(stringBuilder != null){
            em.setMsg(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
        return em;
    }
}
