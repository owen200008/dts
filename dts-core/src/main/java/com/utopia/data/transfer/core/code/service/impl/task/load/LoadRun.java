package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.transfer.TransferData;
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

    public interface LoadRunItem{
        UtopiaErrorCodeClass load(TransferData transferData);

        void close();
    }
    LoadRunItem createItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc);

}
