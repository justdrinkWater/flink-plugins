package com.finchina.plugin.base.constant;

public enum ErrorTypeEnum {
    MESSAGE_ERROR(1, "消息异常"),
    MESSAGE_ERROR_ANALYSIS(11, "解析异常"),
    GET_RELATIVE_PATH_ERROR(12, "相对路径获取异常"),
    GET_GUID_ERROR(13, "guid获取异常"),
    GET_FILE_PATH_ERROR(14, "文件路径获取异常"),
    FILE_GET_ERROR(2, "文件获取或加工失败"),
    SAVE_FILE_LOCAL_ERROR(3, "文件本地保存失败"),
    SEND_MSG_FINISH_ERROR(4, "发送下游上传消息失败");
    private int key;
    private String tip;

    ErrorTypeEnum(int key, String tip) {
        this.key = key;
        this.tip=tip;
    }

    public int getKey() {
        return key;
    }


    public String getTip() {
        return tip;
    }



    public static String getTip(int key){
        for (ErrorTypeEnum value : ErrorTypeEnum.values()) {
            if(key==value.getKey()){
                return value.getTip();
            }
        }
        return "";
    }

    public static ErrorTypeEnum getTypeEnum(int key){
        for (ErrorTypeEnum value : ErrorTypeEnum.values()) {
            if(key==value.getKey()){
                return value;
            }
        }
        return null;
    }

}
