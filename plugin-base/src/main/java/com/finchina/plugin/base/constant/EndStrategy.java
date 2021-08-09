package com.finchina.plugin.base.constant;

public enum EndStrategy {
    FINISH_FOR_COMMON("finish_for_common"),
    FINISH_FOR_AUCTION_FILE("finish_for_auction_file"),
    FINISH_FOR_HWY_GRAP("finish_for_hwy_grap"),
    FINISH_FOR_NOTICE_FILE("finish_for_notice_file"),
    FINISH_WITH_NOTHING("finish_with_nothing");
    private final String endStr;


    EndStrategy(String endStr) {
        this.endStr=endStr;
    }



    public static EndStrategy getEntStrategy(String endStr){

        for (EndStrategy value : values()) {
            if(value.endStr.equals(endStr)){
                return value;
            }
        }

        return null;
    }
}
