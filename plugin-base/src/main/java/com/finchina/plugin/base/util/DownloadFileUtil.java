package com.finchina.plugin.base.util;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Auther: renjianfei
 * @Date: 2020/6/2 10:09
 * @Description:
 */
public class DownloadFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(DownloadFileUtil.class);

    private DownloadFileUtil(){
        throw new IllegalStateException("Utility class");
    }

    //获取文件流
    public static InputStream getFileInputStream(String url) throws IOException {
        InputStream inputStream = null;
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        if (url.toLowerCase().startsWith("http")) {
            try {
                final byte[] bytes = HttpUtil.downloadBytes(url);
                inputStream =  new ByteArrayInputStream(bytes);
            } catch (Exception e) {
                logger.error("down file error {}", url, e);
            }
        } else {
            //判断路径是否为空
            File file = new File(url);
            if (!file.exists()) {
                return null;
            }
            inputStream = new FileInputStream(file);
        }

        return inputStream;
    }


}
