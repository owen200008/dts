package com.utopia.data.transfer.common;

import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.data.transfer.model.code.transfer.TransferUniquePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.validation.constraints.AssertTrue;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/18
 */
public class CommonTest {

    @Test
    public void testUnique(){
        TransferUniqueDesc transferUniqueDesc1=new TransferUniqueDesc();

        transferUniqueDesc1.setSeq(Lists.newArrayList(TransferUniquePair.builder()
                .begin(1L)
                .end(3L)
                .build(),
                TransferUniquePair.builder()
                        .begin(2L)
                        .end(5L)
                        .build(),
                TransferUniquePair.builder()
                        .begin(7L)
                        .end(9L)
                        .build()));

        TransferUniqueDesc transferUniqueDesc2=new TransferUniqueDesc();
        transferUniqueDesc2.setSeq(Lists.newArrayList(TransferUniquePair.builder()
                .begin(1L)
                .end(3L)
                .build(),
                TransferUniquePair.builder()
                        .begin(2L)
                        .end(5L)
                        .build(),
                TransferUniquePair.builder()
                        .begin(6L)
                        .end(9L)
                        .build()));
        transferUniqueDesc1.merge(transferUniqueDesc2);

        System.out.println(transferUniqueDesc1.getSeq());

        Assertions.assertTrue(transferUniqueDesc1.filter(transferUniqueDesc2));
    }
}
