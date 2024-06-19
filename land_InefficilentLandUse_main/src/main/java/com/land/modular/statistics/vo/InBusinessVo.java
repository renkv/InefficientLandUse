package com.land.modular.statistics.vo;

import lombok.Data;

import java.util.List;

@Data
public class InBusinessVo {
    //县代码
    private String xdm;
    //县名称
    private String xmc;
    //年份
    private String year;
    //上报总面积
    private Double totalArea;
    //完成面积
    private Double finishArea;

    private List<String> yearList;
    /**
     * 完成比例
     */
    private Double comratio;
    private Integer flag;
}
