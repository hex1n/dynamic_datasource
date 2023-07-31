package com.dlwlrm4.dynamicds;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author hex1n
 * @Date 2023/7/29/20:48
 * @Description
 **/
@Getter
@Setter
@Data
public class DataSourceEntity implements Serializable {

    private String jdbcUrl;
    private String userName;
    private String password;
    private String driverClassName;
    private Enum databaseType;
}
