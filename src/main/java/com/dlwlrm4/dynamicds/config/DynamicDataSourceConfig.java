package com.dlwlrm4.dynamicds.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author hex1n
 * @Date 2023/8/20/09:08
 * @Description
 **/
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicDataSourceConfig implements TransactionManagementConfigurer {
    private final Map<Object, Object> dataSourceMap = new HashMap<>(8);
    private final DataSourceProperties dataSourceProperties;


    @Bean("dynamicDataSource")
    public DynamicDataSource dataSource() {
        DynamicDataSource ds = new DynamicDataSource();
        DruidDataSource cads = new DruidDataSource();
        cads.setUrl(dataSourceProperties.getUrl());
        cads.setDriverClassName(dataSourceProperties.getDriverClassName());
        cads.setUsername(dataSourceProperties.getUsername());
        cads.setPassword(dataSourceProperties.getPassword());
        ds.setDefaultTargetDataSource(cads);
        ds.setTargetDataSources(dataSourceMap);
        return ds;
    }

    @PostConstruct
    public void init() {
        DruidDataSource dds = new DruidDataSource();
        dds.setUrl(dataSourceProperties.getUrl());
        dds.setDriverClassName(dataSourceProperties.getDriverClassName());
        dds.setUsername(dataSourceProperties.getUsername());
        dds.setPassword(dataSourceProperties.getPassword());

        List<Map<String, Object>> dbList = new JdbcTemplate(dds).queryForList("select * from test.data_source");
        log.info("开始 -> 初始化动态数据源");
        Optional.of(dbList).ifPresent(list -> list.forEach(db -> {
            log.info("数据源:{}", db.get("database_name"));
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(String.valueOf(db.get("jdbc_url")));
            ds.setDriverClassName(String.valueOf(db.get("driver_class_name")));
            ds.setUsername(String.valueOf(db.get("user_name")));
            ds.setPassword(String.valueOf(db.get("password")));
            dataSourceMap.put(db.get("database_name"), ds);
        }));
        log.info("完毕 -> 初始化动态数据源,共计 {} 条", dataSourceMap.size());
    }

    public void reload() {
        init();
        DynamicDataSource dataSource = dataSource();
        dataSource.setTargetDataSources(dataSourceMap);
        dataSource.afterPropertiesSet();
    }


    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }
}
