package com.finchina.plugin.base.flink.rabbitmq.serialize;

import com.finchina.plugin.base.flink.rabbitmq.enums.MessageDeliveryMode;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 15:30
 * @Description
 **/
public class MessageProperties {
    public static final String CONTENT_TYPE_BYTES = "application/octet-stream";

    public static final String X_DELAY = "x-delay";

    public static final String DEFAULT_CONTENT_TYPE = CONTENT_TYPE_BYTES;

    public static final MessageDeliveryMode DEFAULT_DELIVERY_MODE = MessageDeliveryMode.PERSISTENT;

    public static final Integer DEFAULT_PRIORITY = 0;

    private final Map<String, Object> headers = new HashMap<>();

    private Date timestamp;

    private String messageId;

    private String userId;

    private String appId;

    private String clusterId;

    private String type;

    private String correlationId;

    private String replyTo;

    private String contentType = DEFAULT_CONTENT_TYPE;

    private String contentEncoding;

    private long contentLength;

    private boolean contentLengthSet;

    private MessageDeliveryMode deliveryMode = DEFAULT_DELIVERY_MODE;

    private String expiration;

    private Integer priority = DEFAULT_PRIORITY;

    private Boolean redelivered;

    private String receivedExchange;

    private String receivedRoutingKey;

    private String receivedUserId;

    private long deliveryTag;

    private boolean deliveryTagSet;

    private Integer messageCount;

    private String consumerTag;

    private String consumerQueue;

    private Integer receivedDelay;

    private MessageDeliveryMode receivedDeliveryMode;

    private boolean finalRetryForMessageWithNoId;

    private long publishSequenceNumber;

    private boolean lastInBatch;

    private transient Type inferredArgumentType;

    private transient Method targetMethod;

    private transient Object targetBean;

    public void setHeader(String key, Object value) {
        this.headers.put(key, value);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isContentLengthSet() {
        return contentLengthSet;
    }

    public void setContentLengthSet(boolean contentLengthSet) {
        this.contentLengthSet = contentLengthSet;
    }

    public MessageDeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(MessageDeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getRedelivered() {
        return redelivered;
    }

    public void setRedelivered(Boolean redelivered) {
        this.redelivered = redelivered;
    }

    public String getReceivedExchange() {
        return receivedExchange;
    }

    public void setReceivedExchange(String receivedExchange) {
        this.receivedExchange = receivedExchange;
    }

    public String getReceivedRoutingKey() {
        return receivedRoutingKey;
    }

    public void setReceivedRoutingKey(String receivedRoutingKey) {
        this.receivedRoutingKey = receivedRoutingKey;
    }

    public String getReceivedUserId() {
        return receivedUserId;
    }

    public void setReceivedUserId(String receivedUserId) {
        this.receivedUserId = receivedUserId;
    }

    public long getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(long deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    public boolean isDeliveryTagSet() {
        return deliveryTagSet;
    }

    public void setDeliveryTagSet(boolean deliveryTagSet) {
        this.deliveryTagSet = deliveryTagSet;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public String getConsumerQueue() {
        return consumerQueue;
    }

    public void setConsumerQueue(String consumerQueue) {
        this.consumerQueue = consumerQueue;
    }

    public Integer getReceivedDelay() {
        return receivedDelay;
    }

    public void setReceivedDelay(Integer receivedDelay) {
        this.receivedDelay = receivedDelay;
    }

    public MessageDeliveryMode getReceivedDeliveryMode() {
        return receivedDeliveryMode;
    }

    public void setReceivedDeliveryMode(MessageDeliveryMode receivedDeliveryMode) {
        this.receivedDeliveryMode = receivedDeliveryMode;
    }

    public boolean isFinalRetryForMessageWithNoId() {
        return finalRetryForMessageWithNoId;
    }

    public void setFinalRetryForMessageWithNoId(boolean finalRetryForMessageWithNoId) {
        this.finalRetryForMessageWithNoId = finalRetryForMessageWithNoId;
    }

    public long getPublishSequenceNumber() {
        return publishSequenceNumber;
    }

    public void setPublishSequenceNumber(long publishSequenceNumber) {
        this.publishSequenceNumber = publishSequenceNumber;
    }

    public boolean isLastInBatch() {
        return lastInBatch;
    }

    public void setLastInBatch(boolean lastInBatch) {
        this.lastInBatch = lastInBatch;
    }

    public Type getInferredArgumentType() {
        return inferredArgumentType;
    }

    public void setInferredArgumentType(Type inferredArgumentType) {
        this.inferredArgumentType = inferredArgumentType;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(Object targetBean) {
        this.targetBean = targetBean;
    }
}
