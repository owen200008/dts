package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.alibaba.fastjson.JSON;
import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadContext;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectFactory;
import com.utopia.data.transfer.core.code.src.dialect.impl.mysql.MysqlDialect;
import com.utopia.data.transfer.core.code.utils.SqlUtils;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.transfer.TransferData;
import com.utopia.data.transfer.model.code.transfer.TransferEventData;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
@Slf4j
public class LoadRunDB implements LoadRun {

    @UtopiaSPIInject
    private DbDialectFactory dbDialectFactory;

    @Override
    public UtopiaErrorCodeClass load(LoadContext loadContext, TransferData transferData) {
        List<TransferEventData> transferEventData = transferData.getTransferEventData();

        UtopiaErrorCodeClass ret = ErrorCode.CODE_SUCCESS;
        if(isDdlDatas(transferEventData)){
            ret = doDdl(loadContext, transferEventData);
        }
        else{
            ret = doDml(loadContext, transferEventData);
        }
        return ret;
    }

    private UtopiaErrorCodeClass doDml(LoadContext loadContext, List<TransferEventData> transferEventData) {
        DbDialect dbDialect = dbDialectFactory.getDbDialect(loadContext.getPipeline().getId(), loadContext.getPipeline().getTarget());

        LobCreator lobCreator = dbDialect.getLobHandler().getLobCreator();
        try {
            return dbDialect.getTransactionTemplate().execute(status -> {
                for (final TransferEventData data : transferEventData) {
                    try{
                        DataMediaRulePair cacheSourcePairesBySourceId = loadContext.getPipeline().getCacheSourcePairesBySourceId(data.getTableId());

                        DbRuleTemplate extension = UtopiaExtensionLoader.getExtensionLoader(DbRuleTemplate.class).getExtension(cacheSourcePairesBySourceId.getTarget().getDataMediaRuleType().name());

                        List<SqlRunTemplate> sqlRunTemplates = extension.create(cacheSourcePairesBySourceId, data);

                        sqlRunTemplates.forEach(item->{
                            int update = dbDialect.getJdbcTemplate().update(item.getSql(), ps -> doPreparedStatement(ps, dbDialect, lobCreator, item.getColumns()));
                            log.info("run sql update {} {}", item.getSql(), update);
                        });
                    } catch(Throwable e){
                        log.error("doDml run data error", JSON.toJSONString(data));
                        throw new ServiceException(ErrorCode.LOAD_DML_RUN_ERROR);
                    }
                }
                return ErrorCode.CODE_SUCCESS;
            });
        } catch(ServiceException e){
            throw e;
        } catch (Throwable e) {
            log.error("doDml exception", e);
            throw new ServiceException(ErrorCode.LOAD_DML_RUN_ERROR);
        } finally {
            lobCreator.close();
        }
    }

    private void doPreparedStatement(PreparedStatement ps, DbDialect dbDialect, LobCreator lobCreator, List<EventColumn> columns) throws SQLException {
        for (int i = 0; i < columns.size(); i++) {
            int paramIndex = i + 1;
            EventColumn column = columns.get(i);
            int sqlType = column.getColumnType();

            Object param = null;
            if(!column.isNull()){
                if (dbDialect instanceof MysqlDialect
                        && (sqlType == Types.TIME || sqlType == Types.TIMESTAMP || sqlType == Types.DATE)) {
                    // 解决mysql的0000-00-00 00:00:00问题，直接依赖mysql
                    // driver进行处理，如果转化为Timestamp会出错
                    param = column.getColumnValue();
                } else {
                    param = SqlUtils.stringToSqlValue(column.getColumnValue(),
                            sqlType,
                            dbDialect.isEmptyStringNulled());
                }
            }
            switch (sqlType) {
                case Types.CLOB:
                    lobCreator.setClobAsString(ps, paramIndex, (String) param);
                    break;

                case Types.BLOB:
                    lobCreator.setBlobAsBytes(ps, paramIndex, (byte[]) param);
                    break;
                case Types.TIME:
                case Types.TIMESTAMP:
                case Types.DATE:
                    // 只处理mysql的时间类型，oracle的进行转化处理
                    if (dbDialect instanceof MysqlDialect) {
                        // 解决mysql的0000-00-00 00:00:00问题，直接依赖mysql
                        // driver进行处理，如果转化为Timestamp会出错
                        ps.setObject(paramIndex, param);
                    } else {
                        StatementCreatorUtils.setParameterValue(ps, paramIndex, sqlType, null, param);
                    }
                    break;
                case Types.BIT:
                    // 只处理mysql的bit类型，bit最多存储64位，所以需要使用BigInteger进行处理才能不丢精度
                    // mysql driver将bit按照setInt进行处理，会导致数据越界
                    if (dbDialect instanceof MysqlDialect) {
                        StatementCreatorUtils.setParameterValue(ps, paramIndex, Types.DECIMAL, null, param);
                    } else {
                        StatementCreatorUtils.setParameterValue(ps, paramIndex, sqlType, null, param);
                    }
                    break;
                default:
                    StatementCreatorUtils.setParameterValue(ps, paramIndex, sqlType, null, param);
                    break;
            }
        }
    }


    private UtopiaErrorCodeClass doDdl(LoadContext loadContext, List<TransferEventData> transferEventData) {
        DbDialect dbDialect = dbDialectFactory.getDbDialect(loadContext.getPipeline().getId(), loadContext.getPipeline().getTarget());
        //ddl 不支持回滚，只要有失败就全失败
        try {
            for (final TransferEventData data : transferEventData) {
                UtopiaErrorCodeClass execute = dbDialect.getJdbcTemplate().execute((StatementCallback<UtopiaErrorCodeClass>) statement -> {
                    if (dbDialect instanceof MysqlDialect && StringUtils.isNotEmpty(data.getDdlSchemaName())) {
                        // 解决当数据库名称为关键字如"Order"的时候,会报错,无法同步
                        statement.execute("use `" + data.getDdlSchemaName() + "`");
                    }
                    statement.execute(data.getSql());
                    return ErrorCode.CODE_SUCCESS;
                });
                if(execute.getCode() != ErrorCode.CODE_SUCCESS.getCode()) {
                    log.error("run ddl error {} {}", execute.getCode(), JSON.toJSONString(data));
                    return execute;
                }
            }
            return ErrorCode.CODE_SUCCESS;
        } catch (Throwable e) {
            log.error("doDdl exception", e);
            throw new ServiceException(ErrorCode.LOAD_DDL_RUN_ERROR);
        }
    }

    /**
     * 分析整个数据，将datas划分为多个批次. ddl sql前的DML并发执行，然后串行执行ddl后，再并发执行DML
     *
     * @return
     */
    private boolean isDdlDatas(List<TransferEventData> eventDatas) {
        boolean result = false;
        for (TransferEventData eventData : eventDatas) {
            result |= eventData.getEventType().isDdl();
            if (result && !eventData.getEventType().isDdl()) {
                throw new ServiceException(ErrorCode.LOAD_DDL_DATA_ERROR.getCode(), "ddl/dml can't be in one batch, it's may be a bug , pls submit issues.");
            }
        }
        return result;
    }

    private String[] buildColumnNames(List<EventColumn> columns) {
        if(CollectionUtils.isEmpty(columns)){
            return null;
        }
        String[] result = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            EventColumn column = columns.get(i);
            result[i] = column.getColumnName();
        }
        return result;
    }
}
