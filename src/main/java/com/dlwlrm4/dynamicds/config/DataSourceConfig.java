package com.dlwlrm4.dynamicds.config;

import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.mapper.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @Author hex1n
 * @Date 2023/7/29/18:11
 * @Description
 **/
@Configuration
public class DataSourceConfig {


    @Autowired
    private DataSourceMapper dataSourceMapper;

    @PostConstruct
    public void initDataSourcePool() {
       DataSourceFactory.loadDataSource(dataSourceMapper);
    }

}
