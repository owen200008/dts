package com.utopia.data.transfer.admin.hotload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotloadListener {
    
    @Autowired
    DataChangedListener listener;

    public void onDataChanged(String group) {
        listener.onDataChanged(group);
    }
}
