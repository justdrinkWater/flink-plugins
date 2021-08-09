package com.finchina.plugin.base.flink;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeutils.TypeComparator;
import org.apache.flink.api.common.typeutils.TypeSerializer;

/**
 * @Author sunwei@finchina.com
 * @Date 2021/8/4 16:12
 * @Description
 **/
public class BasicTypeInfoExt<T> extends BasicTypeInfo<T> {

    public BasicTypeInfoExt(Class<T> clazz, Class<?>[] possibleCastTargetTypes, TypeSerializer<T> serializer, Class<? extends TypeComparator<T>> comparatorClass) {
        super(clazz, possibleCastTargetTypes, serializer, comparatorClass);
    }
}
