package com.land.modular.statistics.vo;

import lombok.Data;

@Data
public class InBusinessVo {
    //县代码
    private String xdm;
    //县名称
    private String xmc;
    //年份
    private String year;
    //上报总面接
    private Double totalArea;
    //完成面积
    private Double finishArea;
    /**
     * 完成比例
     */
    private Double comratio;
}
