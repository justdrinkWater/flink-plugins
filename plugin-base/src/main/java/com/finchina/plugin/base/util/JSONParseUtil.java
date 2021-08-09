package com.finchina.plugin.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONParseUtil {
    private JSONParseUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static JSONObject parseMsgStr(String messagge) {
        if (messagge.trim().startsWith("{")) {
            return JSON.parseObject(messagge);
        } else if (messagge.trim().startsWith("[")) {
            JSONArray parse = JSON.parseArray(messagge);
            if (!parse.isEmpty()) {
                return parse.getJSONObject(0);
            }
        }
        return null;
    }
}
