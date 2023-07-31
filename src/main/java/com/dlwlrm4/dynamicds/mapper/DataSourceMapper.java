package com.dlwlrm4.dynamicds.mapper;

import com.dlwlrm4.dynamicds.entity.DataSourceEntity;

import java.util.List;

/**
* @author hex1n
* @description 针对表【data_source(数据源表)】的数据库操作Mapper
* @createDate 2023-07-30 13:53:58
* @Entity com.dlwlrm4.dynamicds.entity.DataSourceEntity
*/
public interface DataSourceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DataSourceEntity record);

    int insertSelective(DataSourceEntity record);

    DataSourceEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataSourceEntity record);

    int updateByPrimaryKey(DataSourceEntity record);

    List<DataSourceEntity> findAll();

}
