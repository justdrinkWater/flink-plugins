package com.finchina.plugin.ztb;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.finchina.plugin.base.config.IBaseConfig;
import com.finchina.plugin.base.constant.EndStrategy;
import com.finchina.plugin.base.entity.IMessage;
import com.finchina.plugin.base.flink.rabbitmq.sink.BaseMapFunction;
import com.finchina.plugin.base.util.DownloadFileUtil;
import com.finchina.plugin.base.util.Htmlmodify;
import com.finchina.plugin.base.util.ZLibUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/6 9:28
 * @Description
 **/
public class ZtbMapFunction extends BaseMapFunction {

    public ZtbMapFunction(IBaseConfig baseConfig) {
        super(baseConfig);
    }

    public String getRelativePath(String relativePath, String zlibSuffix) {
        if (StringUtils.isEmpty(relativePath)) {
            return null;
        }
        relativePath = relativePath.replace("\\\\", "/");
        relativePath = relativePath.replaceAll("html$|htm$", zlibSuffix);
        relativePath = relativePath.replaceAll("json$", zlibSuffix);
        return relativePath;
    }

    public EndStrategy endWith() {
        return EndStrategy.getEntStrategy("finish_for_common");
    }

    public InputStream getFileInputStream(IMessage entity) {
        final String filepath = entity.getFilepath();
        if (StringUtils.isEmpty(filepath)) {
            return null;
        }

        InputStream fileInputStream = null;
        try {
            fileInputStream = DownloadFileUtil.getFileInputStream(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == fileInputStream) {
            return null;
        }
        if (filepath.endsWith(".html") || filepath.endsWith(".htm")) {
            String html = IoUtil.read(fileInputStream, StandardCharsets.UTF_8);
            String s = Htmlmodify.formatHtmlAgilityPack(html);
            return new ByteArrayInputStream(ZLibUtils.compress(s.getBytes(StandardCharsets.UTF_8)));
        } else {
            return fileInputStream;
        }
    }

    public String getBucket() {
        return "hwpdf";
    }

    public String generateLocalFilePath(IMessage entity) {
        final String filepath = entity.getFilepath();
        final String relativepath = entity.getRelativepath();
        if (FileNameUtil.extName(filepath).equals(FileNameUtil.extName(relativepath))) {
            return null;
        }
        return getBaseConfig().getLocalDir() + getBaseConfig().getOverallPrefix() + relativepath;
    }

    protected int getPriority() {
        return 11;
    }
}
