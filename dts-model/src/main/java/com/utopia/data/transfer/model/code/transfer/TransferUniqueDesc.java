package com.utopia.data.transfer.model.code.transfer;

import com.utopia.string.UtopiaStringUtil;
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
    private static String SIGN_SPLITE = ":";

    private String key;
    private List<Pair<Long, Long>> seq = new ArrayList();

    public static TransferUniqueDesc parseGtid(String gtid) {
        TransferUniqueDesc ret = new TransferUniqueDesc();
        String[] split = gtid.split(SIGN_SPLITE);
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
        if(UtopiaStringUtil.isBlank(this.key)){
            this.key = parseGtid.getKey();
        }
        if(parseGtid.getSeq().size() == 0){
            return;
        }
        List<Pair<Long, Long>> last = new ArrayList();
        last.addAll(parseGtid.getSeq());
        last.addAll(this.seq);
        last.sort(Comparator.comparing(Pair::getLeft));
        //合并
        this.seq = new ArrayList();
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
            return Pair.of(longLongPair.getLeft(), Math.max(tmp.getRight(), longLongPair.getRight()));
        }
        return null;
    }

    public boolean filter(TransferUniqueDesc gtid) {
        List<Pair<Long, Long>> seq = gtid.getSeq();
        //in才要过滤
        for (int i = 0; i < seq.size(); i++) {
            Pair<Long, Long> longLongPair = seq.get(i);
            boolean in = false;
            for (int j = 0; j < this.seq.size(); j++) {
                if(longLongPair.getLeft() >= this.seq.get(j).getLeft() && longLongPair.getLeft() <= this.seq.get(j).getRight()){
                    if(longLongPair.getRight() <= this.seq.get(j).getRight()){
                        in = true;
                        break;
                    }
                }
            }
            if(!in){
                return false;
            }
        }
        return true;
    }

    public String createUniqueWriteString(){
        StringBuilder ret = new StringBuilder();
        ret.append(this.key);
        ret.append(SIGN_SPLITE);
        for (int i = 0; i < seq.size(); i++) {
            Pair<Long, Long> item = seq.get(i);
            if(item.getLeft().equals(item.getRight())) {
                ret.append(item.getLeft());
            }
            else {
                ret.append(item.getLeft());
                ret.append("-");
                ret.append(item.getRight());
            }
            if(i != seq.size() - 1){
                ret.append(SIGN_SPLITE);
            }
        }
        return ret.toString();
    }
}
