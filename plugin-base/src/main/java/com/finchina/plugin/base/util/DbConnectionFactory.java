package com.finchina.plugin.base.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/6 13:10
 * @Description db 连接工厂
 **/
public class DbConnectionFactory extends BasePooledObjectFactory<Connection> {
    private final DruidDataSource dataSource;

    public DbConnectionFactory() {
        dataSource = new DruidDataSource();
        dataSource.setConnectProperties(PropertyUtil.props);
    }

    @Override
    public Connection create() throws Exception {
        return dataSource.getConnection();
    }

    @Override
    public PooledObject<Connection> wrap(Connection obj) {
        return new DefaultPooledObject<>(obj);
    }
}
