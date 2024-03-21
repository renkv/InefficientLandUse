package com.land.core.constant;

import com.land.sys.core.constant.state.ExpenseState;

public enum LandStatus {
    DATA_NULL(999,"数据不存在或状态不正确"),
    NISHOUCHU(1,"拟收储"),
    CHUBEI(2,"储备"),
    DAICHULI(3,"待处理"),
    CHURANG(4,"出让");

    int code;
    String message;

    LandStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (ExpenseState s : ExpenseState.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
