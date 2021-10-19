package com.utopia.data.transfer.admin.aspect.impl;


import com.utopia.data.transfer.admin.aspect.EventCutAspect;
import com.utopia.data.transfer.admin.aspect.EventCutAspect.EventCutFunction;
import com.utopia.data.transfer.admin.contants.PathConstants;
import com.utopia.data.transfer.admin.hotload.HotloadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * channel cut function
 *
 * @author baoyuliang
 */
@Service
public class ChannelCutFunction implements EventCutFunction {

    @Autowired
    EventCutAspect eventCutAspect;

    @Autowired
    HotloadListener hotloadAction;

    @PostConstruct
    public void init() {
        eventCutAspect.register(this);
    }

    @Override
    public String getKey() {
        return PathConstants.CONFIG_KEY;
    }

    @Override
    public Class getEventCutClass() {
        return Integer.class;
    }

    @Override
    public void doInvoke(Object o, int eventId, int subEventId) {
        Integer ret = (Integer) o;
        if (ret > 0) {
            hotloadAction.onDataChanged(getKey());
        }
    }
}
