package com.finchina.plugin.base.flink.rabbitmq;

import com.finchina.plugin.base.flink.BasicTypeInfoExt;
import com.finchina.plugin.base.flink.rabbitmq.serialize.MessageComparator;
import com.finchina.plugin.base.flink.rabbitmq.serialize.MessageProperties;
import com.finchina.plugin.base.flink.rabbitmq.serialize.MessageSerializer;
import org.apache.flink.api.common.typeinfo.TypeInformation;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 15:30
 * @Description 消息中间件传递的消息
 **/
public class Message implements java.io.Serializable, Comparable<Message> {


    public static final TypeInformation<Message> MESSAGE_TYPE_INFO =
            new BasicTypeInfoExt<>(
                    Message.class,
                    new Class<?>[]{},
                    MessageSerializer.INSTANCE,
                    MessageComparator.class);

    //消息的属性
    private MessageProperties messageProperties;

    //消息体
    private String body;

    public Message() {
    }

    public Message(MessageProperties messageProperties, String body) {
        this.messageProperties = messageProperties;
        this.body = body;
    }

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int compareTo(Message o) {
        return body.compareTo(o.body);
    }
}
