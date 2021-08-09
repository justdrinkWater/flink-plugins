package com.finchina.plugin.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    public static Properties props;

    private PropertyUtil() {

    }

    static {
        loadProps();
    }

    public static void loadProps() {
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            File file = new File("config/application.properties");
            if (file.isFile()) {
                in = new FileInputStream(file);
            } else {
                in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
            }
            props.load(in);
            String profile = props.getProperty("profiles.active");
            if (StringUtils.isNotBlank(profile) && !("***".equals(profile))) {
                in = PropertyUtil.class.getClassLoader().getResourceAsStream("application-" + profile + ".properties");
            }
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("properties文件未找到", e);
        } catch (IOException e) {
            logger.error("出现IOException", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("properties文件流关闭出现异常", e);
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：{}", props);
    }

    public static String getProperty(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }

        if (null == props) {
            loadProps();
        }

        String value = props.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return value;
        }

        return value;
    }

    public static int getPropertyIntValue(String key) {
        String property = getProperty(key);
        if (StringUtils.isBlank(property)) {
            return 0;
        }
        return Integer.parseInt(property);
    }

}