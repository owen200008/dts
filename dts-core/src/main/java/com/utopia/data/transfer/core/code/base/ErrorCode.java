package com.utopia.data.transfer.core.code.base;

import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.data.transfer.core.archetype.base.BaseErrorCode;

/**
 * Created by czq on 2019/6/27.
 */
public class ErrorCode extends BaseErrorCode {
    public final static UtopiaErrorCodeClass CODE_BEGIN = new UtopiaErrorCodeClass(24001, "");
    //first 50 for BaseErrorCode
    //error code define between begin-end 24001 + 50
    public final static UtopiaErrorCodeClass DATAMEDIA_PAIRS_EMPTY = new UtopiaErrorCodeClass(24051, "");
    public final static UtopiaErrorCodeClass CANAL_PARSE_ERROR = new UtopiaErrorCodeClass(24052, "");
    public final static UtopiaErrorCodeClass CANAL_PARSE_DATA_ERROR = new UtopiaErrorCodeClass(24053, "");
    public final static UtopiaErrorCodeClass CANAL_PARSE_DATA_ROW_ERROR = new UtopiaErrorCodeClass(24054, "");
    public final static UtopiaErrorCodeClass DTS_NOFIND_MEDIA = new UtopiaErrorCodeClass(24055, "");
    public final static UtopiaErrorCodeClass DTS_NOFIND_MEDIAPAIR = new UtopiaErrorCodeClass(24056, "");
    public final static UtopiaErrorCodeClass DTS_KEY_COLUMN_NOFIND = new UtopiaErrorCodeClass(24057, "");
    public final static UtopiaErrorCodeClass GET_DB_DIALECT_ERROR = new UtopiaErrorCodeClass(24058, "");

    public final static UtopiaErrorCodeClass NO_SUPPORT_DATASOURCE_TYPE = new UtopiaErrorCodeClass(24100, "");
    public final static UtopiaErrorCodeClass GET_DATASOURCE_ERROR = new UtopiaErrorCodeClass(24101, "");
    public final static UtopiaErrorCodeClass NO_SUPPORT_DIALECT_TYPE = new UtopiaErrorCodeClass(24102, "");
    public final static UtopiaErrorCodeClass CREATE_DIALECT_ERROR = new UtopiaErrorCodeClass(24103, "");
    public final static UtopiaErrorCodeClass NO_FIND_TABLE = new UtopiaErrorCodeClass(24104, "");
    public final static UtopiaErrorCodeClass FIND_TABLE_ERROR = new UtopiaErrorCodeClass(24105, "");
    public final static UtopiaErrorCodeClass GET_TABLE_ERROR = new UtopiaErrorCodeClass(24106, "");


    public final static UtopiaErrorCodeClass INIT_CONFIG_ERROR = new UtopiaErrorCodeClass(24499, "");
    public final static UtopiaErrorCodeClass CODE_END = new UtopiaErrorCodeClass(24500, "");
}
