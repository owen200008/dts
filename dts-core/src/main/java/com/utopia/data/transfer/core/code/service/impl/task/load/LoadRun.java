package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.extension.UtopiaSPI;
import com.utopia.model.rsp.UtopiaErrorCodeClass;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface LoadRun {

    public interface LoadRunItem {
        /**
         * 网络传输
         * @param transferData
         * @return
         */
        UtopiaErrorCodeClass load(Message<TransferEventDataTransaction> transferData);

        /**
         * 内部传递
         * @param message
         * @return
         */
        UtopiaErrorCodeClass loadInner(Message<EventDataTransaction> message);

        void close();


    }
    LoadRunItem createItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc);

}
