package com.finchina.plugin.base.flink;

import com.alibaba.fastjson.JSON;
import com.finchina.plugin.base.flink.rabbitmq.Message;
import com.finchina.plugin.base.flink.rabbitmq.enums.MessageDeliveryMode;
import com.finchina.plugin.base.flink.rabbitmq.serialize.MessageProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.LongString;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.rabbitmq.RMQDeserializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 15:24
 * @Description
 **/
public class RMQDeserializationSchemaImpl implements RMQDeserializationSchema<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final DeserializationSchema<String> schema;

    public RMQDeserializationSchemaImpl(DeserializationSchema<String> deserializationSchema) {
        schema = deserializationSchema;
    }

    @Override
    public void deserialize(
            Envelope envelope,
            AMQP.BasicProperties properties,
            byte[] body,
            RMQCollector<String> collector)
            throws IOException {
        MessageProperties messageProperties = toMessageProperties(properties, envelope, "utf-8");
        Message message = new Message(messageProperties, schema.deserialize(body));
        collector.collect(JSON.toJSONString(message));
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return schema.getProducedType();
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        schema.open(context);
    }

    @Override
    public boolean isEndOfStream(String nextElement) {
        return schema.isEndOfStream(nextElement);
    }


    private MessageProperties toMessageProperties(final AMQP.BasicProperties source, final Envelope envelope,
                                                  final String charset) {
        MessageProperties target = new MessageProperties();
        Map<String, Object> headers = source.getHeaders();
        if (!headers.isEmpty()) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (MessageProperties.X_DELAY.equals(key)) {
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        target.setReceivedDelay((Integer) value);
                    }
                } else {
                    target.setHeader(key, convertLongStringIfNecessary(entry.getValue(), charset));
                }
            }
        }
        target.setTimestamp(source.getTimestamp());
        target.setMessageId(source.getMessageId());
        target.setReceivedUserId(source.getUserId());
        target.setAppId(source.getAppId());
        target.setClusterId(source.getClusterId());
        target.setType(source.getType());
        Integer deliveryMode = source.getDeliveryMode();
        if (deliveryMode != null) {
            target.setReceivedDeliveryMode(MessageDeliveryMode.fromInt(deliveryMode));
        }
        target.setDeliveryMode(null);
        target.setExpiration(source.getExpiration());
        target.setPriority(source.getPriority());
        target.setContentType(source.getContentType());
        target.setContentEncoding(source.getContentEncoding());
        String correlationId = source.getCorrelationId();
        if (StringUtils.isNotBlank(correlationId)) {
            target.setCorrelationId(source.getCorrelationId());
        }
        String replyTo = source.getReplyTo();
        if (replyTo != null) {
            target.setReplyTo(replyTo);
        }
        if (envelope != null) {
            target.setReceivedExchange(envelope.getExchange());
            target.setReceivedRoutingKey(envelope.getRoutingKey());
            target.setRedelivered(envelope.isRedeliver());
            target.setDeliveryTag(envelope.getDeliveryTag());
        }
        return target;
    }

    private Object convertLongStringIfNecessary(Object valueArg, String charset) {
        Object value = valueArg;
        if (value instanceof LongString) {
            value = convertLongString((LongString) value, charset);
        } else if (value instanceof List<?>) {
            List<Object> convertedList = new ArrayList<Object>(((List<?>) value).size());
            for (Object listValue : (List<?>) value) {
                convertedList.add(this.convertLongStringIfNecessary(listValue, charset));
            }
            value = convertedList;
        } else if (value instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<String, Object> originalMap = (Map<String, Object>) value;
            Map<String, Object> convertedMap = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
                convertedMap.put(entry.getKey(), this.convertLongStringIfNecessary(entry.getValue(), charset));
            }
            value = convertedMap;
        }
        return value;
    }

    private Object convertLongString(LongString longString, String charset) {
        try {
            if (longString.length() <= 1024) {
                return new String(longString.getBytes(), charset);
            } else {
                return longString;
            }
        } catch (Exception e) {
            logger.error("convertLongString转换异常", e);
            return longString;
        }
    }
}
