package com.dlwlrm4.dynamicds.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.mapper.DataSourceMapper;
import com.google.common.collect.Lists;
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

    private static ConcurrentHashMap<String, List<DruidDataSource>> druidDataSources = new ConcurrentHashMap<>();

    private static int retryCount = 0;
    private static int maxRetryCount = 3;

    private DataSourceFactory() {

    }

    public static void loadDataSource(DataSourceMapper dataSourceMapper) {

        List<DataSourceEntity> dataSourceEntities = dataSourceMapper.findAll();
        if (!CollectionUtils.isEmpty(dataSourceEntities)) {
            druidDataSources = new ConcurrentHashMap<>();
        }
        log.info("======初始化所有数据连接信息======:{}", JSON.toJSONString(dataSourceEntities));
        for (DataSourceEntity dataSourceEntity : dataSourceEntities) {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(dataSourceEntity.getDriverClassName());
            dataSource.setUrl(dataSourceEntity.getJdbcUrl());
            dataSource.setPassword(dataSourceEntity.getPassword());
            dataSource.setUsername(dataSourceEntity.getUserName());
            if (druidDataSources.containsKey(dataSourceEntity.getDatabaseType().name())) {
                List<DruidDataSource> sources = druidDataSources.get(dataSourceEntity.getDatabaseType().name());
                sources.add(dataSource);
                druidDataSources.put(dataSourceEntity.getDatabaseType().name(), sources);
            } else {
                druidDataSources.put(dataSourceEntity.getDatabaseType().name(), Lists.newArrayList(dataSource));
            }
        }
        log.info("加载数据库连接信息:{} ", druidDataSources.toString());
    }

    public static DruidDataSource matchDataSource(String jdbcUrl, String driverClassName, String userName, String password, String databaseType) {
        List<DruidDataSource> sources = druidDataSources.getOrDefault(databaseType, Lists.newArrayList());
        Optional<DruidDataSource> dataSource = sources.stream()
                .filter((val) -> val.getUrl().equals(jdbcUrl) && val.getDriverClassName().equals(driverClassName)
                        && val.getUsername().equals(userName) && val.getPassword().equals(password)
                ).findFirst();
        if (dataSource.isEmpty() && retryCount < maxRetryCount) {
            retryCount++;
            loadDataSource(SpringContextHelper.getBean(DataSourceMapper.class));
            log.info("matchDataSource 重试:{} 次", retryCount);
            return matchDataSource(jdbcUrl, driverClassName, userName, password, databaseType);
        } else {
            retryCount = 0;
        }
        return dataSource.orElseThrow(() -> new IllegalArgumentException("未匹配到数据连接"));
    }
}
