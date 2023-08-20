package com.dlwlrm4.dynamicds.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author hex1n
 * @Date 2023/8/20/09:05
 * @Description
 **/
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 指定路由key
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("获取数据源：{}", DynamicDataSourceContextHolder.getDataSourceType());
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
