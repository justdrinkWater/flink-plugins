package com.finchina.plugin.base.flink.rabbitmq.serialize;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.typeutils.SimpleTypeSerializerSnapshot;
import org.apache.flink.api.common.typeutils.TypeSerializerSnapshot;
import org.apache.flink.api.common.typeutils.base.TypeSerializerSingleton;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;
import com.finchina.plugin.base.flink.rabbitmq.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 16:02
 * @Description
 **/
public class MessageSerializer extends TypeSerializerSingleton<Message> {


    public static final MessageSerializer INSTANCE = new MessageSerializer();

    @Override
    public boolean isImmutableType() {
        return false;
    }

    @Override
    public Message createInstance() {
        return new Message();
    }

    @Override
    public Message copy(Message from) {
        if (from == null) {
            return null;
        }
        return new Message(from.getMessageProperties(), from.getBody());
    }

    @Override
    public Message copy(Message from, Message reuse) {
        if (from == null) {
            return null;
        }
        reuse.setBody(from.getBody());
        reuse.setMessageProperties(from.getMessageProperties());
        return reuse;
    }

    @Override
    public int getLength() {
        return -1;
    }

    @Override
    public void serialize(Message record, DataOutputView target) throws IOException {
        byte[] bytes = JSON.toJSONString(record).getBytes(StandardCharsets.UTF_8);
        target.writeInt(bytes.length);
        target.write(bytes);
    }

    @Override
    public Message deserialize(DataInputView source) throws IOException {
        Message message = new Message();
        return deserialize(message, source);
    }

    @Override
    public Message deserialize(Message reuse, DataInputView source) throws IOException {
        int l = source.readInt();
        byte[] bodyByte = new byte[l];
        source.read(bodyByte);
        String str = new String(bodyByte, StandardCharsets.UTF_8);
        return JSON.parseObject(str, Message.class);
    }

    @Override
    public void copy(DataInputView source, DataOutputView target) throws IOException {
        Message message = deserialize(source);
        serialize(message, target);
    }

    @Override
    public TypeSerializerSnapshot<Message> snapshotConfiguration() {
        return new MessageSerializerSnapshot();
    }

    public static final class MessageSerializerSnapshot
            extends SimpleTypeSerializerSnapshot<Message> {

        public MessageSerializerSnapshot() {
            super(() -> INSTANCE);
        }
    }
}
