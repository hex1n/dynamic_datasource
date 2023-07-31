package com.dlwlrm4.dynamicds.service.impl;

import com.dlwlrm4.dynamicds.config.DataSourceFactory;
import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.mapper.DataSourceMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author hex1n
 * @Date 2023/7/30/16:35
 * @Description
 **/
@Service
@Slf4j
public class DsDynamicServiceImpl implements DsDynamicService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<DataSourceEntity> findAll() {

        return dataSourceMapper.findAll();
    }

    @Override
    public Object switchDatabase(DataSourceEntity dataSourceEntity) {
        HikariDataSource dataSource = DataSourceFactory.matchDataSource(dataSourceEntity.getJdbcUrl(), dataSourceEntity.getDriverClassName(), dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(), dataSourceEntity.getDatabaseName());
        jdbcTemplate.setDataSource(dataSource);

        List<Map<String, Object>> result = jdbcTemplate.queryForList("select  * from t_user");
        return result;
    }


}
