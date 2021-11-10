package com.utopia.data.transfer.model.code.transfer;

import com.utopia.string.UtopiaStringUtil;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
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
public class TransferUniqueDesc implements Serializable {
    private static String SIGN_SPLITE = ":";

    private String key;
    private List<TransferUniquePair> seq = new ArrayList();

    public static TransferUniqueDesc parseGtid(String gtid) {
        TransferUniqueDesc ret = new TransferUniqueDesc();
        String[] split = gtid.split(SIGN_SPLITE);
        int length = split.length;
        if(length < 2){
            return null;
        }
        ret.setKey(split[0]);

        List<TransferUniquePair> set = new ArrayList();
        for (int i = 1; i < split.length; i++) {
            String[] split1 = split[i].split("-");
            if(split.length == 1){
                long l = Long.parseLong(split1[0]);
                set.add(TransferUniquePair.builder()
                        .begin(l)
                        .end(l)
                        .build());
            }
            else{
                set.add(TransferUniquePair.builder()
                        .begin(Long.parseLong(split1[0]))
                        .end(Long.parseLong(split1[1]))
                        .build());
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
        List<TransferUniquePair> last = new ArrayList();
        last.addAll(parseGtid.getSeq());
        last.addAll(this.seq);
        last.sort(Comparator.comparing(TransferUniquePair::getBegin));
        //合并
        this.seq = new ArrayList();
        TransferUniquePair longLongPair = last.get(0);
        for (int i = 1; i < last.size(); i++) {
            //判断是否连续
            TransferUniquePair tmp = last.get(i);
            TransferUniquePair combin = isCombin(longLongPair, tmp);
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

    private TransferUniquePair isCombin(TransferUniquePair longLongPair, TransferUniquePair tmp) {
        if(tmp.getBegin() <= longLongPair.getEnd() + 1){
            return TransferUniquePair.builder()
                    .begin(longLongPair.getBegin())
                    .end(Math.max(tmp.getEnd(), longLongPair.getEnd()))
                    .build();
        }
        return null;
    }

    public boolean filter(TransferUniqueDesc gtid) {
        List<TransferUniquePair> seq = gtid.getSeq();
        //in才要过滤
        for (int i = 0; i < seq.size(); i++) {
            TransferUniquePair longLongPair = seq.get(i);
            boolean in = false;
            for (int j = 0; j < this.seq.size(); j++) {
                if(longLongPair.getBegin() >= this.seq.get(j).getBegin() && longLongPair.getBegin() <= this.seq.get(j).getEnd()){
                    if(longLongPair.getEnd() <= this.seq.get(j).getEnd()){
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
            TransferUniquePair item = seq.get(i);
            if(item.getBegin() == item.getEnd()) {
                ret.append(item.getBegin());
            }
            else {
                ret.append(item.getBegin());
                ret.append("-");
                ret.append(item.getEnd());
            }
            if(i != seq.size() - 1){
                ret.append(SIGN_SPLITE);
            }
        }
        return ret.toString();
    }


    public boolean isSame(TransferUniqueDesc o) {
        if(!key.equals(o.getKey())){
            return false;
        }
        if(seq.size() != o.getSeq().size()){
            return false;
        }
        for (int i = 0; i < seq.size(); i++) {
            TransferUniquePair src = seq.get(i);
            TransferUniquePair dst = o.getSeq().get(i);

            if(!(src.getBegin() == dst.getBegin() && src.getEnd() == dst.getEnd())){
                return false;
            }
        }
        return true;
    }
}
