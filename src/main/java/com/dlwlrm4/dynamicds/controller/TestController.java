package com.dlwlrm4.dynamicds.controller;

import com.dlwlrm4.dynamicds.entity.DataSourceEntity;
import com.dlwlrm4.dynamicds.service.DsDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author hex1n
 * @Date 2023/7/30/15:17
 * @Description
 **/
@RestController()
public class TestController {

    @Autowired
    private DsDynamicService dsDynamicService;

    @GetMapping("/hello")
    public String test() {
        System.out.println("this is test method");
        return "hello";
    }

    @GetMapping("/findAll")
    public List<DataSourceEntity> findAll() {
        return dsDynamicService.findAll();
    }

    @GetMapping("/switchDatabase")
    public Object switchDatabase() {
        List<DataSourceEntity> all = dsDynamicService.findAll();
        DataSourceEntity dataSourceEntity = all.get(1);
        //dataSourceEntity.setDatabaseType(DatabaseType.HIVE);
        return dsDynamicService.switchDatabase(dataSourceEntity);
    }

    @PostMapping("/addDatabase")
    public DataSourceEntity addDatabase() {
        DataSourceEntity dataSourceEntity = new DataSourceEntity();
        List<DataSourceEntity> all = dsDynamicService.findAll();
        dataSourceEntity.setId(String.valueOf(System.nanoTime()));
        DataSourceEntity dataSource = all.get(1);
        dataSourceEntity.setJdbcUrl("jdbc:mysql://localhost:3306/assistant");
        dataSourceEntity.setDatabaseType(dataSource.getDatabaseType());
        dataSourceEntity.setUserName(dataSource.getUserName());
        dataSourceEntity.setPassword(dataSource.getPassword());
        dataSourceEntity.setDriverClassName(dataSource.getDriverClassName());
        dataSourceEntity.setDatabaseName("assistant");
        dataSourceEntity.setCreateBy(dataSource.getCreateBy());
        dataSourceEntity.setUpdateBy(dataSource.getUpdateBy());
        dataSourceEntity.setCreateTime(new Date());
        dataSourceEntity.setUpdateTime(new Date());

        return  dsDynamicService.addDatabase(dataSourceEntity);

    }


}
