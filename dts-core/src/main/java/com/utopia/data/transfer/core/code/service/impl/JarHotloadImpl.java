package com.utopia.data.transfer.core.code.service.impl;

import com.utopia.data.transfer.core.code.service.JarHotload;
import com.utopia.utils.HotloadJarEnum;
import com.utopia.utils.JarUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/11/17
 * @alter_author
 * @alter_date
 */
@Service
public class JarHotloadImpl implements JarHotload {
    private String path;

    @Override
    public void init(String path, List<String> ayList) {
        this.path = path;

        for (String s : ayList) {
            JarUtils.getInstance().loadJar(path, HotloadJarEnum.LOCAL.getValue(), s);
        }
    }

    @Override
    public void loadRemote(String jar) {
        JarUtils.getInstance().loadJar(path, HotloadJarEnum.REMOTE.getValue(), jar);
    }
}
