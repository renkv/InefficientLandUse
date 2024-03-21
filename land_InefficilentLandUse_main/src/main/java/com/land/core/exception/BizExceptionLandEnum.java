package com.land.core.exception;

import cn.stylefeng.roses.kernel.model.exception.AbstractBaseExceptionEnum;

/**
 * @author xiaobai
 * @Description 土地业务异常的枚举
 * @date 2020年04月22日 下午5:04:51
 */
public enum BizExceptionLandEnum implements AbstractBaseExceptionEnum {

    /**
     * 租户相关的异常
     */
    NO_LANDBASEINFO_ERROR(1000, "没有相关土地基本信息"),
    NO_NISHOUCHU_ERROR(1001, "没有相关拟收储土地信息"),
    NO_CHUBEI_ERROR(1002, "没有相关储备土地信息"),
    NO_DAICHULI_ERROR(1003, "没有相关待处理土地信息");

    BizExceptionLandEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
