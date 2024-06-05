package com.land.modular.plan.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class LandPlanExcelParam implements Serializable {
    @Excel(name = "区县编码")
    private String countyCode;
    @Excel(name = "区县名称")
    private String countyName;
    @Excel(name = "年份")
    private String year;
    @Excel(name = "开发区名称")
    private String zoneName;
    @Excel(name = "社会统一信用代码")
    private String creditCode;
    @Excel(name = "企业名称")
    private String busName;
    @Excel(name = "占地面积（亩）")
    private Double occupyArea;
    @Excel(name = "坐落")
    private String located;
    @Excel(name = "上年度亩均税收")
    private Double preYearTax;
    @Excel(name = "上上年度亩均税收")
    private Double upYearTax;
    @Excel(name = "是否规上企业")
    private String isPlanBus;
    @Excel(name = "目前进展")
    private String curProgress;
    @Excel(name = "企业经营状态")
    private String busStatus;
    @Excel(name = "现状用途")
    private String useStatus;
    @Excel(name = "处置类型")
    private String disType;
    @Excel(name = "认定标准")
    private String conStandard;
    @Excel(name = "处置标准")
    private String disStandard;
}
