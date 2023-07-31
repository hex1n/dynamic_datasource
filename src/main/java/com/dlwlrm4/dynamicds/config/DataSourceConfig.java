package com.dlwlrm4.dynamicds;

import com.google.common.collect.Maps;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author hex1n
 * @Date 2023/7/29/18:11
 * @Description
 **/
@Configuration
public class DataSourceConfig {
    private Map<String, DataSourceEntity> dataSources = Maps.newHashMap();

    public Map<String, DataSourceEntity> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, DataSourceEntity> dataSources) {
        this.dataSources = dataSources;
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        DynamicDataSourceRouter dataSourceRouter = new DynamicDataSourceRouter();
        Map<@Nullable Object, @Nullable Object> targetDataSources = Maps.newHashMap();
        for (Map.Entry<String, DataSourceEntity> entry : dataSources.entrySet()) {
            DataSourceEntity dataSourceEntity = entry.getValue();
            DataSource dataSource = createDataSource(dataSourceEntity);
            targetDataSources.put(entry.getKey(), dataSource);
        }
        dataSourceRouter.setTargetDataSources(targetDataSources);
        return dataSourceRouter;
    }

    private DataSource createDataSource(DataSourceEntity dataSourceEntity) {
        // Implement the creation of DataSource based on the properties for each database type
        // You can use DataSourceBuilder or any other method to create the DataSource
        // Example:
        return DataSourceBuilder.create()
                .driverClassName(dataSourceEntity.getDriverClassName())
                .url(dataSourceEntity.getJdbcUrl())
                .username(dataSourceEntity.getUserName())
                .password(dataSourceEntity.getPassword())
                .build();

    }
}
