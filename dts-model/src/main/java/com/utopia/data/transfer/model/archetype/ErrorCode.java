package com.utopia.data.transfer.model.archetype;

import com.utopia.model.rsp.UtopiaErrorCodeClass;

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
    public final static UtopiaErrorCodeClass CANAL_PARSE_GTID_ERROR = new UtopiaErrorCodeClass(24059, "");
    public final static UtopiaErrorCodeClass NO_TOKEN = new UtopiaErrorCodeClass(24060, "");

    public final static UtopiaErrorCodeClass NO_SUPPORT_DATASOURCE_TYPE = new UtopiaErrorCodeClass(24100, "");
    public final static UtopiaErrorCodeClass GET_DATASOURCE_ERROR = new UtopiaErrorCodeClass(24101, "");
    public final static UtopiaErrorCodeClass NO_SUPPORT_DIALECT_TYPE = new UtopiaErrorCodeClass(24102, "");
    public final static UtopiaErrorCodeClass CREATE_DIALECT_ERROR = new UtopiaErrorCodeClass(24103, "");
    public final static UtopiaErrorCodeClass NO_FIND_TABLE = new UtopiaErrorCodeClass(24104, "");
    public final static UtopiaErrorCodeClass FIND_TABLE_ERROR = new UtopiaErrorCodeClass(24105, "");
    public final static UtopiaErrorCodeClass GET_TABLE_ERROR = new UtopiaErrorCodeClass(24106, "");

    public static final UtopiaErrorCodeClass DERIALIZATION_FORMAT_ERROR = new UtopiaErrorCodeClass(24110,"DERIALIZATION_FORMAT_ERROR");
    public static final UtopiaErrorCodeClass DERIALIZATION_NOFIND_SERIALIZE = new UtopiaErrorCodeClass(24111,"DERIALIZATION_NOFIND_SERIALIZE");
    public static final UtopiaErrorCodeClass DERIALIZATION_UNSUPPORT_SERIALIZE = new UtopiaErrorCodeClass(24112,"DERIALIZATION_UNSUPPORT_SERIALIZE");
    public static final UtopiaErrorCodeClass DERIALIZATION_VERSION_ERROR = new UtopiaErrorCodeClass(24113,"DERIALIZATION_VERSION_ERROR");


    public final static UtopiaErrorCodeClass CHILD_NEED_DELETE_FIRST = new UtopiaErrorCodeClass(24201, "this not first delete");
    public final static UtopiaErrorCodeClass SOURCE_TYPE_ERROR = new UtopiaErrorCodeClass(24202, "");
    public final static UtopiaErrorCodeClass SOURCE_DATAMEDIA_NO_FIND = new UtopiaErrorCodeClass(24203, "");
    public final static UtopiaErrorCodeClass TARGET_DATAMEDIA_NO_FIND = new UtopiaErrorCodeClass(24204, "");

    public final static UtopiaErrorCodeClass LOAD_NO_TOPIC_CONFIG = new UtopiaErrorCodeClass(24373, "");
    public final static UtopiaErrorCodeClass ERROR_EXIT = new UtopiaErrorCodeClass(24374, "");
    public final static UtopiaErrorCodeClass LOAD_NO_KAFKA = new UtopiaErrorCodeClass(24375, "");
    public final static UtopiaErrorCodeClass LOAD_NO_SERIALIZATION = new UtopiaErrorCodeClass(24376, "");
    public final static UtopiaErrorCodeClass LOAD_SOURCE_DATA_NO_SUPPORT = new UtopiaErrorCodeClass(24377, "");
    public final static UtopiaErrorCodeClass LOAD_RUN_CLOSED = new UtopiaErrorCodeClass(24378, "");
    public final static UtopiaErrorCodeClass LOAD_RUN_EXCEPTION = new UtopiaErrorCodeClass(24379, "");

    public final static UtopiaErrorCodeClass LOAD_DML_RUN_ERROR = new UtopiaErrorCodeClass(24389, "");

    public final static UtopiaErrorCodeClass LOAD_RULE_CREATE_FAIL = new UtopiaErrorCodeClass(24393, "");
    public final static UtopiaErrorCodeClass LOAD_RULE_NO_FIND = new UtopiaErrorCodeClass(24394, "");
    public final static UtopiaErrorCodeClass LOAD_CREATE_SYNCRULE_ERR = new UtopiaErrorCodeClass(24395, "");
    public final static UtopiaErrorCodeClass LOAD_CREATE_ERR = new UtopiaErrorCodeClass(24396, "");
    public final static UtopiaErrorCodeClass LOAD_DDL_RUN_ERROR = new UtopiaErrorCodeClass(24397, "");
    public final static UtopiaErrorCodeClass LOAD_DDL_DATA_ERROR = new UtopiaErrorCodeClass(24398, "");
    public final static UtopiaErrorCodeClass LOAD_GET_EXTENSION_FAIL = new UtopiaErrorCodeClass(24399, "");
    public final static  UtopiaErrorCodeClass PARSE_OBJECT_FAIL = new UtopiaErrorCodeClass(24400, "");

    public final static UtopiaErrorCodeClass SELECT_NO_SERIALIZATION = new UtopiaErrorCodeClass(24491, "");
    public static final UtopiaErrorCodeClass FIELD_NAME_IS_FAIL = new UtopiaErrorCodeClass(24492,"field name is error!");
    public static final UtopiaErrorCodeClass LOGIN_FAIL = new UtopiaErrorCodeClass(24493,"login fail");
    public final static UtopiaErrorCodeClass SELECT_RULE_NO_SELECT_DATA = new UtopiaErrorCodeClass(24494, "");
    public final static UtopiaErrorCodeClass SELECT_DISPATH_EXCEPTION = new UtopiaErrorCodeClass(24495, "");
    public final static UtopiaErrorCodeClass SELECT_DISPATH_FACADE_NOFIND = new UtopiaErrorCodeClass(24496, "");
    public final static UtopiaErrorCodeClass SELECT_RULE_INIT_FAIL = new UtopiaErrorCodeClass(24497, "");
    public final static UtopiaErrorCodeClass SELECT_RULE_NO_FIND = new UtopiaErrorCodeClass(24498, "");
    public final static UtopiaErrorCodeClass INIT_CONFIG_ERROR = new UtopiaErrorCodeClass(24499, "");
    public final static UtopiaErrorCodeClass CODE_END = new UtopiaErrorCodeClass(24500, "");


}
