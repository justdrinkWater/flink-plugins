package com.finchina.plugin.base.entity;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.finchina.plugin.base.constant.Constance;
import com.finchina.plugin.base.constant.ErrorTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @Auther: renjianfei
 * @Date: 2020/4/16 15:13
 * @Description:文件上传日志类
 */
public class FileCdnRecord implements Serializable, IMessage {
    // 主键id
    private Integer id;
    // rabbitmq中guid
    private String guid;
    // 初始文件地址
    private String filepath;
    // 华为云下载地址
    private String url;
    // 文件大小
    private Long filesize;
    // 消息处理状态  状态（0：成功，1：获取文件异常，2：文件上传异常，3：kafka消息发送异常）
    private int state;

    // 消息处理状态  状态（0：未重试，1：已重发，2已重试）
    private int retryFlag;
    // 错误原因
    private String memo;

    // 消息内容
    private String message;

    // 路由key
    private String routekey;
    //文件桶内相对路径
    private String relativepath;

    private LocalDateTime sendtime;

    private LocalDateTime createtime;

    public static FileCdnRecord initRecord(String msgStr, String routeKey, JSONObject msgJson) {
        FileCdnRecord fileCdnRecord = new FileCdnRecord();
        fileCdnRecord.setMessage(msgStr);
        fileCdnRecord.setRoutekey(routeKey);
        if (msgJson == null) {
            fileCdnRecord.setState(ErrorTypeEnum.MESSAGE_ERROR_ANALYSIS.getKey());
            fileCdnRecord.setMemo(ErrorTypeEnum.MESSAGE_ERROR_ANALYSIS.getTip());
            return fileCdnRecord;
        }
        //如果是重试任务，消息体内会有retry_table_id字段
        final Integer integer = msgJson.getInteger(Constance.RETRY_TABLE_ID);
        if (integer != null && integer > 0) {
            fileCdnRecord.setId(integer);
            //说明第二次消费该任务
            fileCdnRecord.setRetryFlag(2);
        }
        Long sendStamp = msgJson.getLong(Constance.MESSAGE_SEND_TIME);
        if (sendStamp != null && sendStamp > 0) {
            fileCdnRecord.setSendtime(LocalDateTimeUtil.of(sendStamp));
        }
        String guid = msgJson.getString(Constance.MESSAGE_FILE_GUID);
        if (StringUtils.isEmpty(guid)) {
            fileCdnRecord.setState(ErrorTypeEnum.GET_GUID_ERROR.getKey());
            fileCdnRecord.setMemo(ErrorTypeEnum.GET_GUID_ERROR.getTip());
            return fileCdnRecord;
        }
        fileCdnRecord.setGuid(guid);

        final String filePath = msgJson.getString(Constance.MESSAGE_FILE_PATH);
        if (StringUtils.isEmpty(filePath)) {
            fileCdnRecord.setState(ErrorTypeEnum.GET_FILE_PATH_ERROR.getKey());
            fileCdnRecord.setMemo(ErrorTypeEnum.GET_FILE_PATH_ERROR.getTip());
            return fileCdnRecord;
        }
        fileCdnRecord.setFilepath(filePath);
        String relativePath = msgJson.getString(Constance.MESSAGE_RELATIVE_PATH);
        if (StringUtils.isEmpty(relativePath)) {
            fileCdnRecord.setState(ErrorTypeEnum.GET_RELATIVE_PATH_ERROR.getKey());
            fileCdnRecord.setMemo(ErrorTypeEnum.GET_RELATIVE_PATH_ERROR.getTip());
            return fileCdnRecord;
        } else {
            fileCdnRecord.setRelativepath(relativePath);
        }
        return fileCdnRecord;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(int retryFlag) {
        this.retryFlag = retryFlag;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoutekey() {
        return routekey;
    }

    public void setRoutekey(String routekey) {
        this.routekey = routekey;
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
    }

    public LocalDateTime getSendtime() {
        return sendtime;
    }

    public void setSendtime(LocalDateTime sendtime) {
        this.sendtime = sendtime;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }
}
