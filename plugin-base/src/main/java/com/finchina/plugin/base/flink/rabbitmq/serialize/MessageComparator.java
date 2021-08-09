package com.finchina.plugin.base.flink.rabbitmq.serialize;

import org.apache.flink.api.common.typeutils.TypeComparator;
import org.apache.flink.api.common.typeutils.base.BasicTypeComparator;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.MemorySegment;
import org.apache.flink.types.StringValue;
import com.finchina.plugin.base.flink.rabbitmq.Message;

import java.io.IOException;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 16:03
 * @Description
 **/
public final class MessageComparator extends BasicTypeComparator<Message> {

    private static final int HIGH_BIT = 0x1 << 7;

    private static final int HIGH_BIT2 = 0x1 << 13;

    private static final int HIGH_BIT2_MASK = 0x3 << 6;

    public MessageComparator(boolean ascending) {
        super(ascending);
    }


    @Override
    public int compareSerialized(DataInputView firstSource, DataInputView secondSource) throws IOException {
        String s1 = StringValue.readString(firstSource);
        String s2 = StringValue.readString(secondSource);
        int comp = s1.compareTo(s2);
        return ascendingComparison ? comp : -comp;
    }

    @Override
    public boolean supportsNormalizedKey() {
        return true;
    }

    @Override
    public int getNormalizeKeyLen() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isNormalizedKeyPrefixOnly(int keyBytes) {
        return true;
    }

    @Override
    public void putNormalizedKey(Message message, MemorySegment target, int offset, int len) {
        String record = message.getBody();
        final int limit = offset + len;
        final int end = record.length();
        int pos = 0;

        while (pos < end && offset < limit) {
            char c = record.charAt(pos++);
            if (c < HIGH_BIT) {
                target.put(offset++, (byte) c);
            } else if (c < HIGH_BIT2) {
                target.put(offset++, (byte) ((c >>> 7) | HIGH_BIT));
                if (offset < limit) {
                    target.put(offset++, (byte) c);
                }
            } else {
                target.put(offset++, (byte) ((c >>> 10) | HIGH_BIT2_MASK));
                if (offset < limit) {
                    target.put(offset++, (byte) (c >>> 2));
                }
                if (offset < limit) {
                    target.put(offset++, (byte) c);
                }
            }
        }
        while (offset < limit) {
            target.put(offset++, (byte) 0);
        }
    }

    @Override
    public TypeComparator<Message> duplicate() {
        return new MessageComparator(ascendingComparison);
    }
}
