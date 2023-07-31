package com.dlwlrm4.dynamicds.config;

import com.alibaba.fastjson.JSON;
import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.mapper.DataSourceMapper;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author hex1n
 * @Date 2023/7/30/20:19
 * @Description
 **/
@Slf4j
@Getter
@Setter
@Component
public class DataSourceFactory {

    private static ConcurrentHashMap<String, List<HikariDataSource>> hikariDataSources = new ConcurrentHashMap<>();

    private static int retryCount = 0;
    private static int maxRetryCount = 3;

    private DataSourceFactory() {

    }

    public static void loadDataSource(DataSourceMapper dataSourceMapper) {

        List<DataSourceEntity> dataSourceEntities = dataSourceMapper.findAll();
        if (!CollectionUtils.isEmpty(dataSourceEntities)) {
            hikariDataSources = new ConcurrentHashMap<>();
        }
        log.info("======初始化所有数据连接信息======:{}", JSON.toJSONString(dataSourceEntities));
        for (DataSourceEntity dataSourceEntity : dataSourceEntities) {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(dataSourceEntity.getDriverClassName());
            dataSource.setJdbcUrl(dataSourceEntity.getJdbcUrl());
            dataSource.setPassword(dataSourceEntity.getPassword());
            dataSource.setUsername(dataSourceEntity.getUserName());
            dataSource.setSchema(dataSourceEntity.getDatabaseName());
            if (hikariDataSources.containsKey(dataSourceEntity.getDatabaseType().name())) {
                List<HikariDataSource> sources = hikariDataSources.get(dataSourceEntity.getDatabaseType().name());
                sources.add(dataSource);
                hikariDataSources.put(dataSourceEntity.getDatabaseType().name(), sources);
            } else {
                hikariDataSources.put(dataSourceEntity.getDatabaseType().name(), Lists.newArrayList(dataSource));
            }
        }
        log.info("加载数据库连接信息:{} ", hikariDataSources.toString());
    }

    public static HikariDataSource matchDataSource(String jdbcUrl, String driverClassName, String userName, String password, String databaseName, String databaseType) {
        List<HikariDataSource> sources = hikariDataSources.getOrDefault(databaseType, Lists.newArrayList());
        Optional<HikariDataSource> dataSource = sources.stream()
                .filter((val) -> val.getJdbcUrl().equals(jdbcUrl) && val.getDriverClassName().equals(driverClassName)
                        && val.getUsername().equals(userName) && val.getPassword().equals(password) && val.getSchema().equals(databaseName)
                ).findFirst();
        if (dataSource.isEmpty() && retryCount < maxRetryCount) {
            retryCount++;
            loadDataSource(SpringContextHelper.getBean(DataSourceMapper.class));
            log.info("matchDataSource 重试:{} 次", retryCount);
            return matchDataSource(jdbcUrl, driverClassName, userName, password, databaseName, databaseType);
        } else {
            retryCount = 0;
        }
        return dataSource.orElseThrow(() -> new IllegalArgumentException("未匹配到数据连接"));
    }
}
