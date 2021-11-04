package com.utopia.data.transfer.core.code.base.config;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public interface DTSConstants {



    /**
     * 在logback的配置文件中定义好的按照各个pipeline在load时，归档输出的键值.
     */
    public String splitPipelineLoadLogFileKey   = "load";

    /**
     * 在logback的配置文件中定义好的按照各个pipeline在select时，归档输出的键值.
     */
    public String splitPipelineSelectLogFileKey = "select";

    /**
     * 注册的ip
     */
    public final String GET_PUBLIC_IP_DOMAIN = "ip.blurams.cn";

    /**
     * 读取的配置
     */
    public static final String CONFIG_KEY = "KERNEL";

    /**
     * 读取的配置
     */
    public static final String CONF_PATH_DUBBO = "utopia.dts.dubbo";
}
