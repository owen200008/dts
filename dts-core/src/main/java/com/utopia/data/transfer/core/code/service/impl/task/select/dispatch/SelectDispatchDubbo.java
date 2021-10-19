package com.utopia.data.transfer.core.code.service.impl.task.select.dispatch;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadTransferFacade;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDispatchRule;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.transfer.TransferData;
import com.utopia.data.transfer.model.code.transfer.TransferEventData;
import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Slf4j
public class SelectDispatchDubbo implements SelectDispatchRule {

    private static UtopiaResponseModel DUBBO_FACADE_ERROR = UtopiaResponseModel.fail(ErrorCode.SELECT_DISPATH_FACADE_NOFIND);
    private static UtopiaResponseModel DUBBO_EXCEPTION_ERROR = UtopiaResponseModel.fail(ErrorCode.SELECT_DISPATH_EXCEPTION);
    private ConcurrentHashMap<Long, LoadTransferFacade> mapTransfer = new ConcurrentHashMap();


    @Override
    public boolean init(String dispatchRuleParam) {
        return true;
    }

    @Override
    public CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventDataTransaction> message) {
        //根据pipeline获取服务
        try{
            LoadTransferFacade loadTransferFacade = mapTransfer.get(pipeline.getId());
            if(Objects.isNull(loadTransferFacade)){
                loadTransferFacade = getFacadeObject(LoadTransferFacade.class, LoadTaskImpl.TRANSFER_VERSION, String.valueOf(pipeline.getId()));
                if(Objects.isNull(loadTransferFacade)) {
                    log.error("no facade find {}", pipeline.getId());
                    return CompletableFuture.completedFuture(DUBBO_FACADE_ERROR);
                }
                mapTransfer.put(pipeline.getId(), loadTransferFacade);
            }
            if(loadTransferFacade != null){
                //传递数据
                List<EventDataTransaction> datas = message.getDatas();
                TransferData transferData = new TransferData();
                transferData.setId(message.getId());
                transferData.setTransferEventData(transferTransfer(datas));
                return loadTransferFacade.transfer(transferData);

            }
        }catch(Throwable e){
            log.error("dispatch error", e);
        }
        return CompletableFuture.completedFuture(DUBBO_EXCEPTION_ERROR);
    }

    private List<TransferEventDataTransaction> transferTransfer(List<EventDataTransaction> datas) {
        return datas.stream().map(data->{
            TransferEventDataTransaction transferEventData = new TransferEventDataTransaction();
            transferEventData.setGtid(data.getGtid());
            transferEventData.setDatas(data.getDatas().stream().map(item->{
                TransferEventData tmp = new TransferEventData();
                tmp.setTableId(item.getTableId());
                tmp.setEventType(item.getEventType());
                tmp.setExecuteTime(item.getExecuteTime());

                tmp.setOldKeys(item.getOldKeys());
                tmp.setKeys(item.getKeys());
                tmp.setColumns(item.getColumns());

                tmp.setSql(item.getSql());
                tmp.setDdlSchemaName(item.getDdlSchemaName());
                return tmp;
            }).collect(Collectors.toList()));
            return transferEventData;
        }).collect(Collectors.toList());
    }


    private <T> T getFacadeObject(Class<T> clazz, String version, String group) {
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setInterface(clazz);
        reference.setVersion(version);
        reference.setGroup(group);
        T ret = null;
        try{
            ret = reference.get();
        }catch(Exception e){
            log.error("getFacadeObject error", e);
        }
        return ret;
    }
}
