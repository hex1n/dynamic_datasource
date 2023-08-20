package com.dlwlrm4.dynamicds.service;

import com.dlwlrm4.dynamicds.entity.DataSourceEntity;

import java.util.List;

/**
 * @Author hex1n
 * @Date 2023/7/30/16:34
 * @Description
 **/
public interface DsDynamicService {

    List<DataSourceEntity> findAll();

    Object switchDatabase(DataSourceEntity dataSourceEntity);

    DataSourceEntity addDatabase(DataSourceEntity dataSourceEntity);

    DataSourceEntity addDatabase2(DataSourceEntity dataSourceEntity);

    Object switchDatabase2(DataSourceEntity dataSource);
}
