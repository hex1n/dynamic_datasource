package com.dlwlrm4.dynamicds.entity;

import com.dlwlrm4.dynamicds.constant.DatabaseType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author hex1n
 * @Date 2023/7/29/20:48
 * @Description
 **/
@Getter
@Setter
@Data
public class DataSourceEntity implements Serializable {

    private String id;
    private String jdbcUrl;
    private String userName;
    private String password;
    private String databaseName;
    private String driverClassName;
    private DatabaseType databaseType;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
