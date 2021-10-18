package com.utopia.data.transfer.common;

import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
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

        transferUniqueDesc1.setSeq(Lists.newArrayList(Pair.of(1L,3L),Pair.of(2L,5L),Pair.of(7L,9L)));

        TransferUniqueDesc transferUniqueDesc2=new TransferUniqueDesc();
        transferUniqueDesc2.setSeq(Lists.newArrayList(Pair.of(1L,3L),Pair.of(2L,5L),Pair.of(7L,9L)));
        transferUniqueDesc1.merge(transferUniqueDesc2);

        System.out.println(transferUniqueDesc1.getSeq());

        Assertions.assertTrue(transferUniqueDesc1.filter(transferUniqueDesc2));
    }
}
