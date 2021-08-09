package com.finchina.plugin.base.flink.rabbitmq.enums;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 15:51
 * @Description
 **/
public enum MessageDeliveryMode {
    NON_PERSISTENT,

    /**
     * Persistent.
     */
    PERSISTENT;

    public static int toInt(MessageDeliveryMode mode) {
        switch (mode) {
            case NON_PERSISTENT:
                return 1;
            case PERSISTENT:
                return 2;
            default:
                return -1;
        }
    }

    public static MessageDeliveryMode fromInt(int modeAsNumber) {
        switch (modeAsNumber) {
            case 1:
                return NON_PERSISTENT;
            case 2:
                return PERSISTENT;
            default:
                return null;
        }
    }
}
