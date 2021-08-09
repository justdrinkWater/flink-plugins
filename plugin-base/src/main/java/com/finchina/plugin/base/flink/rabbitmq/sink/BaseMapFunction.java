package com.finchina.plugin.base.flink.rabbitmq.sink;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finchina.plugin.base.config.BaseConfig;
import com.finchina.plugin.base.config.IBaseConfig;
import com.finchina.plugin.base.constant.EndStrategy;
import com.finchina.plugin.base.constant.ErrorTypeEnum;
import com.finchina.plugin.base.entity.FileCdnRecord;
import com.finchina.plugin.base.entity.IMessage;
import com.finchina.plugin.base.entity.NextMessage;
import com.finchina.plugin.base.flink.rabbitmq.Message;
import com.finchina.plugin.base.flink.rabbitmq.serialize.MessageProperties;
import com.finchina.plugin.base.util.FileHandleUtil;
import com.finchina.plugin.base.util.JSONParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 17:11
 * @Description
 **/
public abstract class BaseMapFunction implements MapFunction<String, FileCdnRecord> {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    private final IBaseConfig baseConfig;

    public BaseMapFunction(IBaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    @Override
    public FileCdnRecord map(String str) throws Exception {
        FileCdnRecord cdnRecord = null;
        try {
            Message message = JSON.parseObject(str, Message.class);
            MessageProperties messageProperties = message.getMessageProperties();
            String receivedRoutingKey = messageProperties.getReceivedRoutingKey();
            String value = message.getBody();
            //获取消息优先等级
            JSONObject msgJson = JSONParseUtil.parseMsgStr(value);
            //初始化数据并进行相关校验
            cdnRecord = FileCdnRecord.initRecord(value, receivedRoutingKey, msgJson);
            if (cdnRecord.getState() != 0) {
                return cdnRecord;
            }
            //相对路径进行加工处理
            final String relativePath = baseConfig.getOverallPrefix() + this.getRelativePath(cdnRecord.getRelativepath(), baseConfig.getZlibSuffix());
            cdnRecord.setRelativepath(relativePath);

            NextMessage nextMessage = new NextMessage();
            nextMessage.setJsonObject(msgJson);
            nextMessage.setRouteKey(receivedRoutingKey);
            nextMessage.setRelativepath(relativePath);

            //校验源文件是否存在,获取并加工文件
            InputStream fileInputStream = this.getFileInputStream(cdnRecord);

            if (null == fileInputStream) {
                cdnRecord.setState(ErrorTypeEnum.FILE_GET_ERROR.getKey());
                cdnRecord.setMemo(ErrorTypeEnum.FILE_GET_ERROR.getTip());
                return cdnRecord;
            }

            String url;
            //判断文件是否需要写入本地
            final String needSaveLocal = generateLocalFilePath(cdnRecord);
            if (StringUtils.isEmpty(needSaveLocal)) {
                nextMessage.setFilepath(cdnRecord.getFilepath());
            } else {
                url = FileHandleUtil.saveFileToLocal(needSaveLocal, fileInputStream);
                if (StringUtils.isEmpty(url)) {
                    cdnRecord.setState(ErrorTypeEnum.SAVE_FILE_LOCAL_ERROR.getKey());
                    cdnRecord.setMemo(ErrorTypeEnum.SAVE_FILE_LOCAL_ERROR.getTip());
                    return cdnRecord;
                }
                nextMessage.setFilepath(url);
                cdnRecord.setUrl(url);
            }

            long filesize = FileUtil.size(new File(nextMessage.getFilepath()));
            cdnRecord.setFilesize(filesize);
            nextMessage.setFilesize(filesize);

            //下发后续处理策略
            //下发上传目标桶
            nextMessage.setBucket(this.getBucket());
            nextMessage.setEndWith(this.endWith().toString());

            logger.info("send rabbitmq message");
            //后续处理是否成功
            if (!sendMessage(baseConfig.getNextQueue(), nextMessage, this.getPriority())) {
                cdnRecord.setState(ErrorTypeEnum.SEND_MSG_FINISH_ERROR.getKey());
                cdnRecord.setMemo(ErrorTypeEnum.SEND_MSG_FINISH_ERROR.getTip());
            }
        } catch (Exception e) {
            logger.error("pre handle task rabbitmq consumer error", e);
            if (cdnRecord != null) {
                cdnRecord.setState(ErrorTypeEnum.MESSAGE_ERROR.getKey());
                cdnRecord.setMemo(ErrorTypeEnum.MESSAGE_ERROR.getTip());
            }
        }
        return cdnRecord;
    }

    public IBaseConfig getBaseConfig() {
        return baseConfig;
    }

    public abstract String getRelativePath(String relativePath, String zlibSuffix);

    public abstract EndStrategy endWith();

    public abstract InputStream getFileInputStream(IMessage entity);

    public abstract String getBucket();

    public String generateLocalFilePath(IMessage entity) {
        final String filepath = entity.getFilepath();
        final String relativepath = entity.getRelativepath();
        if (FileNameUtil.extName(filepath).equals(FileNameUtil.extName(relativepath))) {
            return null;
        }
        return baseConfig.getLocalDir() + baseConfig.getOverallPrefix() + relativepath;
    }

    protected int getPriority() {
        return 11;
    }

    protected boolean sendMessage(String nextQueue, NextMessage nextMessage, int priority) {
        return false;
    }
}
