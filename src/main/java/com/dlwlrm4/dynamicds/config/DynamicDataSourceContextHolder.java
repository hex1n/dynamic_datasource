package com.dlwlrm4.dynamicds.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author hex1n
 * @Date 2023/8/20/09:07
 * @Description
 **/
@Slf4j
public class DynamicDataSourceContextHolder {

    private DynamicDataSourceContextHolder() {

    }

    private static final ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前线程的数据源变量
     */
    public static void setDataSourceType(String dataSourceType) {
        log.info("已切换到{}数据源", dataSourceType);
        DATA_SOURCE_HOLDER.set(dataSourceType);
    }

    /**
     * 获取当前线程的数据源变量
     */
    public static String getDataSourceType() {
        String key = DATA_SOURCE_HOLDER.get();
        return key == null ? "test" : key;
    }

    /**
     * 删除与当前线程绑定的数据源变量
     */
    public static void removeDataSourceType() {
        log.info("移除数据源：{}", DATA_SOURCE_HOLDER.get());
        DATA_SOURCE_HOLDER.remove();
    }
}
