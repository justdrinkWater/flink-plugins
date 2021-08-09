package com.finchina.plugin.ztb;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.finchina.plugin.base.entity.FileCdnRecord;
import com.finchina.plugin.base.util.DbUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/7/30 14:03
 * @Description
 **/
public class MySqlSink extends RichSinkFunction<List<FileCdnRecord>> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Connection connection = null;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = DbUtils.getConnection();
    }

    @Override
    public void invoke(List<FileCdnRecord> fileCdnRecordList, Context context) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO prehandle_record_daily (guid, filepath, url, filesize, state, retryFlag, memo, message, routekey, sendtime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  ON DUPLICATE KEY UPDATE url= ?, filesize= ?, state = ?,retryFlag= ?,memo= ?");
            for (FileCdnRecord cdnRecord : fileCdnRecordList) {
                preparedStatement.setString(1, cdnRecord.getGuid());
                preparedStatement.setString(2, cdnRecord.getFilepath());
                preparedStatement.setString(3, cdnRecord.getUrl());
                preparedStatement.setLong(4, Objects.isNull(cdnRecord.getFilesize()) ? 0 : cdnRecord.getFilesize());
                preparedStatement.setInt(5, cdnRecord.getState());
                preparedStatement.setInt(6, cdnRecord.getRetryFlag());
                preparedStatement.setString(7, cdnRecord.getMemo());
                preparedStatement.setString(8, cdnRecord.getMessage());
                preparedStatement.setString(9, cdnRecord.getRoutekey());
                preparedStatement.setString(10, Objects.nonNull(cdnRecord.getSendtime()) ? LocalDateTimeUtil.format(cdnRecord.getSendtime(), "yyyy-MM-dd HH:mm:ss") : null);
                preparedStatement.setString(11, cdnRecord.getUrl());
                preparedStatement.setLong(12, Objects.isNull(cdnRecord.getFilesize()) ? 0 : cdnRecord.getFilesize());
                preparedStatement.setInt(13, cdnRecord.getState());
                preparedStatement.setInt(14, cdnRecord.getRetryFlag());
                preparedStatement.setString(15, cdnRecord.getMemo());
                preparedStatement.addBatch();
            }

            //一次性写入
            int[] ints = preparedStatement.executeBatch();
            logger.info("一次写入长度，{}", ints.length);
        } catch (Exception e) {
            logger.error("保存入库异常", e);
        } finally {
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }

    }

    @Override
    public void close() throws Exception {
        super.close();
        DbUtils.returnToPool(connection);
    }
}
