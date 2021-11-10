package com.utopia.data.transfer.model.code.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/11/10
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferUniquePair implements Serializable {
    private long begin;
    private long end;
}
