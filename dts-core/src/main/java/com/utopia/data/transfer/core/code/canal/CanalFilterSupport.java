package com.utopia.data.transfer.core.code.canal;

import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.model.code.data.media.DataMedia;
import com.utopia.data.transfer.model.code.data.media.DataMediaPair;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
public class CanalFilterSupport {

    /**
     * 构建filter 表达式
     */
    public static String makeFilterExpression(Pipeline pipeline) {
        List<DataMediaPair> dataMediaPairs = pipeline.getPairs();
        if (dataMediaPairs.isEmpty()) {
            throw new ServiceException(ErrorCode.DATAMEDIA_PAIRS_EMPTY.getCode(), "ERROR ## the pair is empty,the pipeline id = " + pipeline.getId());
        }

        Set<String> mediaNames = new HashSet();
        for (DataMediaPair dataMediaPair : dataMediaPairs) {
            buildFilter(mediaNames, dataMediaPair.getSource());
        }

        StringBuilder result = new StringBuilder();
        Iterator<String> iter = mediaNames.iterator();
        int i = -1;
        while (iter.hasNext()) {
            i++;
            if (i == 0) {
                result.append(iter.next());
            } else {
                result.append(",").append(iter.next());
            }
        }

//        String markTable = pipeline.getParams().getSystemSchema() + "."
//                + pipeline.getParams().getSystemMarkTable();
//        String bufferTable = pipeline.getParams().getSystemSchema() + "."
//                + pipeline.getParams().getSystemBufferTable();
//        String dualTable = pipeline.getParams().getSystemSchema() + "."
//                + pipeline.getParams().getSystemDualTable();
//
//        if (!mediaNames.contains(markTable)) {
//            result.append(",").append(markTable);
//        }
//
//        if (!mediaNames.contains(bufferTable)) {
//            result.append(",").append(bufferTable);
//        }
//
//        if (!mediaNames.contains(dualTable)) {
//            result.append(",").append(dualTable);
//        }

        return result.toString();
    }

    private static void buildFilter(Set<String> mediaNames, DataMedia dst) {
        String splitChar = ".";
        mediaNames.add(dst.getNamespace() + splitChar + dst.getValue());
    }
}
