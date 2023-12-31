package com.dlwlrm4.dynamicds.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.dlwlrm4.dynamicds.config.DataSourceFactory;
import com.dlwlrm4.dynamicds.config.DynamicDataSourceConfig;
import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.mapper.DataSourceMapper;
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
    private DynamicDataSourceConfig dataSourceConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<DataSourceEntity> findAll() {

        return dataSourceMapper.findAll();
    }

    @Override
    public Object switchDatabase(DataSourceEntity dataSourceEntity) {
        DruidDataSource dataSource = DataSourceFactory.matchDataSource(dataSourceEntity.getJdbcUrl(),
                dataSourceEntity.getDriverClassName(), dataSourceEntity.getUserName(),
                dataSourceEntity.getPassword(), dataSourceEntity.getDatabaseType().name());
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

    @Override
    public DataSourceEntity addDatabase2(DataSourceEntity dataSourceEntity) {
        dataSourceMapper.insert(dataSourceEntity);
        dataSourceConfig.reload();
        return dataSourceEntity;
    }

    @Override
    public Object switchDatabase2(DataSourceEntity dataSource) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select  * from t_user");
        log.info("result:{}", JSON.toJSONString(result));
        return result;
    }


}
