package com.finchina.plugin.base.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finchina.plugin.base.constant.Constance;


public class NextMessage {

    private String guid;
    private String filepath;
    private String relativepath;
    private Long filesize;
    private String endWith;
    private String bucket;
    private String routeKey;
    private JSONObject jsonObject;


    public String getMsgStr() {
        if (routeKey == null) return "";
        jsonObject.put(Constance.MESSAGE_ROUTE_KEY, routeKey);
        if (filepath == null) return "";
        jsonObject.put(Constance.MESSAGE_FILE_PATH, filepath);
        if (relativepath == null) return "";
        jsonObject.put(Constance.MESSAGE_RELATIVE_PATH, relativepath);
        jsonObject.put(Constance.MESSAGE_FILE_SIZE, filesize);
        jsonObject.put(Constance.MESSAGE_HANDLE_END, endWith);
        if (bucket == null) return "";
        jsonObject.put(Constance.MESSAGE_TARGET_BUCKET, bucket);
        jsonObject.put(Constance.MESSAGE_SEND_TIME, System.currentTimeMillis());
        return JSON.toJSONString(jsonObject);
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public String getEndWith() {
        return endWith;
    }

    public void setEndWith(String endWith) {
        this.endWith = endWith;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.guid = jsonObject.getString(Constance.MESSAGE_FILE_GUID);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }


    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    @Override
    public String toString() {
        return "NextMessage{" +
                "guid='" + guid + '\'' +
                ", filepath='" + filepath + '\'' +
                ", relativepath='" + relativepath + '\'' +
                ", filesize=" + filesize +
                ", endWith='" + endWith + '\'' +
                ", bucket='" + bucket + '\'' +
                ", routeKey='" + routeKey + '\'' +
                ", jsonObject=" + jsonObject +
                '}';
    }
}
