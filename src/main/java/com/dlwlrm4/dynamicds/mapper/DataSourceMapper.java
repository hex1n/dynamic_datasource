package generator.mapper;

import generator.domain.DataSource;

/**
* @author hex1n
* @description 针对表【data_source(数据源表)】的数据库操作Mapper
* @createDate 2023-07-30 13:53:58
* @Entity generator.domain.DataSource
*/
public interface DataSourceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DataSource record);

    int insertSelective(DataSource record);

    DataSource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataSource record);

    int updateByPrimaryKey(DataSource record);

}
