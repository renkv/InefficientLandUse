package com.land.modular.plan.vo;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class LandPlanInfoVo {
    private String id;

    private String landCode;

    private String planType;

    private String planName;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Long createUser;

    private Long updateUser;

    private String planStartTime;

    private String planEndTime;
    private String xmmc;

    private String actStartTime;

    private String actEndTime;

    private Double planArea;

    private Double currentArea;

    private Double remArea;

    private String curStatus;

    private String curProgress;

    private String reasons;

    private String reasonsType;

    private String villageName;

    private String renewalName;

    private String zoneName;

    private String planUnit;

    private String actUnit;

    private String creditCode;

    private String busName;

    private Double occupyArea;

    private String located;

    private Double preYearTax;

    private Double upYearTax;

    private String isPlanBus;

    private String busStatus;

    private String remark;
}
