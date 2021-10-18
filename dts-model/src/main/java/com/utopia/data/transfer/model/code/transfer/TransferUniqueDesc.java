package com.utopia.data.transfer.model.code.transfer;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author owen.cai
 * @create_date 2021/10/18
 * @alter_author
 * @alter_date
 */
@Data
public class TransferUniqueDesc {
    private String key;
    private List<Pair<Long, Long>> seq = new ArrayList();

    public static TransferUniqueDesc parseGtid(String gtid) {
        TransferUniqueDesc ret = new TransferUniqueDesc();
        String[] split = gtid.split(":");
        int length = split.length;
        if(length < 2){
            return null;
        }
        ret.setKey(split[0]);

        List<Pair<Long, Long>> set = new ArrayList();
        for (int i = 1; i < split.length; i++) {
            String[] split1 = split[i].split("-");
            if(split.length == 1){
                long l = Long.parseLong(split1[0]);

                set.add(Pair.of(l, l));
            }
            else{
                set.add(Pair.of(Long.parseLong(split1[0]), Long.parseLong(split1[1])));
            }
        }
        ret.setSeq(set);
        return ret;
    }

    public void merge(TransferUniqueDesc parseGtid) {
        if(parseGtid.getSeq().size() == 0){
            return;
        }
        List<Pair<Long, Long>> last = new ArrayList();
        last.addAll(parseGtid.getSeq());
        last.addAll(this.seq);
        last.sort(Comparator.comparing(Pair::getLeft));
        //合并
        Pair<Long, Long> longLongPair = last.get(0);
        for (int i = 1; i < last.size(); i++) {
            //判断是否连续
            Pair<Long, Long> tmp = last.get(i);
            Pair combin = isCombin(longLongPair, tmp);
            if(Objects.isNull(combin)){
                this.seq.add(longLongPair);
                longLongPair = tmp;
            }
            else{
                longLongPair = combin;
            }
        }
        this.seq.add(longLongPair);
    }

    private Pair isCombin(Pair<Long, Long> longLongPair, Pair<Long, Long> tmp) {
        if(tmp.getLeft() <= longLongPair.getRight() + 1){
            return Pair.of(longLongPair.getLeft(), tmp.getRight());
        }
        return null;
    }
}
