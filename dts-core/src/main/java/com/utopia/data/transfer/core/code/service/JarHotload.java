package com.utopia.data.transfer.core.code.service;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/11/17
 * @alter_author
 * @alter_date
 */
public interface JarHotload {

    void init(String path, List<String> ayList);

    /**
     *
     * @param jar
     */
    void loadRemote(String jar);
}
