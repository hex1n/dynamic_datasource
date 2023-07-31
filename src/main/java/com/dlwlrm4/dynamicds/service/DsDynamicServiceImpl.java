package com.dlwlrm4.dynamicds.service;

import com.alibaba.fastjson.JSON;
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
        HikariDataSource dataSource = DataSourceFactory.matchDataSource(dataSourceEntity.getJdbcUrl(),
                dataSourceEntity.getDriverClassName(), dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(), dataSourceEntity.getDatabaseName(),
                dataSourceEntity.getDatabaseType().name());
        jdbcTemplate.setDataSource(dataSource);

        List<Map<String, Object>> result = jdbcTemplate.queryForList("select  * from t_user");
        log.info("result:{}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public DataSourceEntity addDatabase(DataSourceEntity dataSourceEntity) {
        dataSourceMapper.insert(dataSourceEntity);
        DataSourceFactory.loadDataSource(dataSourceMapper);
        return dataSourceEntity;
    }


}
