package com.finchina.plugin.base.config;


import java.io.Serializable;

public class BaseConfig implements IBaseConfig, Serializable {

    private String zlibSuffix = "zlib";
    private String overallPrefix;
    private String localDir = "E:/obs/file/";
    private String nextQueue = "cdn_upload_queue";

    @Override
    public String getZlibSuffix() {
        return zlibSuffix;
    }

    public void setZlibSuffix(String zlibSuffix) {
        this.zlibSuffix = zlibSuffix;
    }

    @Override
    public String getOverallPrefix() {
        return overallPrefix;
    }

    public void setOverallPrefix(String overallPrefix) {
        this.overallPrefix = overallPrefix;
    }

    @Override
    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    @Override
    public String getNextQueue() {
        return nextQueue;
    }

    public void setNextQueue(String nextQueue) {
        this.nextQueue = nextQueue;
    }
}
