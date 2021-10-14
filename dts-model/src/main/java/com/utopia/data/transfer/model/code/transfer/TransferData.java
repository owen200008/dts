package com.utopia.data.transfer.model.code.transfer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/12
 * @alter_author
 * @alter_date
 */
@Data
public class TransferData implements Serializable {
    private Long    id;
    private List<TransferEventData> transferEventData;
}
