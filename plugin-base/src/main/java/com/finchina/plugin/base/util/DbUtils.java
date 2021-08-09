package com.finchina.plugin.base.util;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/6 10:33
 * @Description
 **/
public class DbUtils {

    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

    private static GenericObjectPool<Connection> pool;

    static {
        init();
    }

    public static void init() {
        if (pool == null || pool.isClosed()) {
            synchronized (DbUtils.class) {
                if (pool == null || pool.isClosed()) {
                    DbConnectionFactory connectionFactory = new DbConnectionFactory();
                    // 基本配置
                    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                    config.setMaxTotal(PropertyUtil.getPropertyIntValue("connection.total"));  //连接池最大数量
                    config.setMaxIdle(PropertyUtil.getPropertyIntValue("connection.maxIde"));   //最大空闲数量
                    config.setMinIdle(PropertyUtil.getPropertyIntValue("connection.minIde"));    //最小空闲数量
                    config.setTestOnBorrow(true);  //在从pool中取对象时进行有效性检查，会调用工厂中的validateObject
                    config.setMaxWaitMillis(PropertyUtil.getPropertyIntValue("connection.waitMillis")); //提取对象时最大等待时间，超时会抛出异常
                    config.setSoftMinEvictableIdleTimeMillis(10000); // 最小的空闲对象驱除时间间隔，空闲超过指定的时间的对象，会被清除掉
                    config.setTimeBetweenEvictionRunsMillis(10000);// 后台驱逐线程休眠时间
                    config.setNumTestsPerEvictionRun(PropertyUtil.getPropertyIntValue("connection.clean")); // 设置驱逐线程每次检测对象的数量

                    // 泄漏清理配置
                    AbandonedConfig abandonedConfig = new AbandonedConfig();
                    abandonedConfig.setRemoveAbandonedOnMaintenance(true); // 在Maintenance的时候检查是否有泄漏
                    abandonedConfig.setRemoveAbandonedOnBorrow(true); // borrow的时候检查泄漏
                    abandonedConfig.setRemoveAbandonedTimeout(PropertyUtil.getPropertyIntValue("connection.timeout")); // 如果一个对象borrow之后10秒还没有返还给pool，认为是泄漏的对象
                    // 创建连接池
                    pool = new GenericObjectPool<>(connectionFactory, config, abandonedConfig);
                    pool.setTimeBetweenEvictionRunsMillis(2000); // 2秒运行一次维护任务
                }
            }
        }
    }

    public static Connection getConnection() {
        Connection connection;
        try {
            connection = pool.borrowObject();
            return connection;
        } catch (Exception e) {
            logger.error("getConnection has Exception:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 返还对象到对象池中
     */
    public static void returnToPool(Connection connection) {
        try {
            if (null != connection) {
                pool.returnObject(connection);
            }
        } catch (Exception e) {
            logger.error("returnToPool error", e);
        }
    }

}
