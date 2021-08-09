package com.finchina.plugin.ztb;

import com.finchina.plugin.base.config.BaseConfig;
import com.finchina.plugin.base.entity.FileCdnRecord;
import com.finchina.plugin.base.flink.RMQDeserializationSchemaImpl;
import com.finchina.plugin.base.util.DbUtils;
import com.finchina.plugin.base.util.PropertyUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.apache.flink.util.Collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/7/30 13:55
 * @Description
 **/
public class ZtbMain {
    public static void main(String[] args) throws Exception {
        //从配置文件中读取配置
        BaseConfig baseConfig = new BaseConfig();

        MySqlSink mySqlSink = new MySqlSink();

        ZtbMapFunction ztbMapFunction = new ZtbMapFunction(baseConfig);

        //1.获取flink的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
                .setHost("10.17.207.78")   //地址
                .setPort(5672)
                .setUserName("admin")     //别用默认的，自己创建一个用户，注意用户权限
                .setPassword("finchina")
                .setVirtualHost("/")
                .build();

        //2.连接socket获取输入的数据(数据源Data Source)
        DataStreamSource<String> dataStreamSource = env.addSource(new RMQSource<>(connectionConfig,
                "sunwei_sw_queue_demo", true, new RMQDeserializationSchemaImpl(new SimpleStringSchema())));

        //3.数据转换
        DataStream<FileCdnRecord> map = dataStreamSource.map(ztbMapFunction);

        //4.收集5s内的数据再sink 不要使用lambda表达式，会报错
        map.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5L))).
                apply(new AllWindowFunction<FileCdnRecord, List<FileCdnRecord>, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow window, Iterable<FileCdnRecord> iterable, Collector<List<FileCdnRecord>> out) throws Exception {
                        List<FileCdnRecord> records = Lists.newArrayList(iterable.iterator());
                        if (!records.isEmpty()) {
                            System.out.println("5秒的总共收到的条数：" + records.size());
                            out.collect(records);
                        }
                    }
                }).addSink(mySqlSink);//sink 到数据库

        //5.执行程序
        env.execute("ztb-job");

    }

}
