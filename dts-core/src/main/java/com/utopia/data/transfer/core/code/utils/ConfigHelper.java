package com.utopia.data.transfer.core.code.utils;

import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.model.code.data.media.DataMedia;
import com.utopia.data.transfer.model.code.data.media.DataMediaPair;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import org.apache.commons.lang3.StringUtils;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
public class ConfigHelper {

    /**
     * 根据NameSpace和Name得到对应的DataMedia.
     */
    public static DataMedia findSourceDataMedia(Pipeline pipeline, String namespace,
                                                String name) {
        return findSourceDataMedia(pipeline, namespace, name, false);
    }

    /**
     * 根据NameSpace和Name得到对应的DataMedia
     */
    public static DataMedia findSourceDataMedia(Pipeline pipeline, String namespace,
                                                                           String name, boolean notExistReturnNull) {
        for (DataMediaPair pair : pipeline.getPairs()) {
            if (isMatch(pair.getSource(), namespace, name)) {
                return pair.getSource();
            }
        }

        if (notExistReturnNull) {
            return null;
        } else {
            throw new ServiceException(ErrorCode.DTS_NOFIND_MEDIA.getCode(), "no such DataMedia , the namespace = " + namespace + " name = " + name);
        }
    }

    /**
     * 根据NameSpace和Name得到对应的DataMediaPair.
     */
    public static DataMediaPair findDataMediaPairBySourceName(Pipeline pipeline, String namespace, String name) {
        return findDataMediaPairBySourceName(pipeline, namespace, name, false);
    }

    /**
     * 根据NameSpace和Name得到对应的DataMediaPair
     */
    public static DataMediaPair findDataMediaPairBySourceName(Pipeline pipeline, String namespace, String name,
                                                              boolean notExistReturnNull) {
        for (DataMediaPair pair : pipeline.getPairs()) {
            if (isMatch(pair.getSource(), namespace, name)) {
                return pair;
            }
        }

        if (notExistReturnNull) {
            return null;
        } else {
            throw new ServiceException(ErrorCode.DTS_NOFIND_MEDIAPAIR.getCode(), "no such DataMedia , the namespace = " + namespace + " name = " + name);
        }
    }

    private static boolean isMatch(DataMedia dataMedia, String namespace, String name) {
        boolean isMatch = true;
        if (StringUtils.isEmpty(namespace)) {
            isMatch &= StringUtils.isEmpty(dataMedia.getNamespace());
        } else {
            isMatch &= dataMedia.getNamespace().equalsIgnoreCase(namespace);
        }

        if (StringUtils.isEmpty(name)) {
            isMatch &= StringUtils.isEmpty(dataMedia.getValue());
        } else {
            isMatch &= dataMedia.getValue().equalsIgnoreCase(name);
        }
        return isMatch;
    }
}
