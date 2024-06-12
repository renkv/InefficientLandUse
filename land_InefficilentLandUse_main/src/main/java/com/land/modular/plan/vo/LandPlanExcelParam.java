package com.land.modular.plan.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@Data
public class LandPlanExcelParam implements Serializable {
/*    @Excel(name = "区县编码")
    private String countyCode;*/
    @Excel(name = "区县名称")
    @NotEmpty(message = "县名称不能为空")
    private String countyName;
    @Excel(name = "年份")
    @NotEmpty(message = "年份不能为空")
    private String year;
    @Excel(name = "企业名称")
    @NotEmpty(message = "企业名称不能为空")
    private String busName;
    @Excel(name = "开发区名称")
    @NotEmpty(message = "开发区名称不能为空")
    private String zoneName;
    @Excel(name = "社会统一信用代码")
    @NotEmpty(message = "社会统一信用代码不能为空")
    private String creditCode;

    @Excel(name = "占地面积（公顷）")
    @NotEmpty(message = "占地面积不能为空")
    private Double occupyArea;
    @Excel(name = "坐落")
    @NotEmpty(message = "坐落不能为空")
    private String located;
    @Excel(name = "上年度亩均税收")
    private Double preYearTax;
    @Excel(name = "上上年度亩均税收")
    private Double upYearTax;
/*    @Excel(name = "是否规上企业")
    private String isPlanBus;*/

/*    @Excel(name = "企业经营状态")
    @NotEmpty(message = "企业经营状态不能为空")
    private String busStatus;*/
    @Excel(name = "现状用途")
    @NotEmpty(message = "现状用途不能为空")
    private String useStatus;
/*    @Excel(name = "处置类型")
    private String disType;*/
    @Excel(name = "低效类型")
    @NotEmpty(message = "低效类型不能为空")
    private String conStandard;
    @Excel(name = "处置方式")
    @NotEmpty(message = "处置方式不能为空")
    private String disStandard;

    @Excel(name = "当前状态")
    @NotEmpty(message = "当前状态不能为空")
    private String curStatus;
    @Excel(name = "详细进展")
    private String curProgress;
    @Excel(name = "处置面积（公顷）")
    private Double currentArea;
    @Excel(name = "计划实施单位")
    private String planUnit;
    @Excel(name = "问题类型")
    private String reasonsType;
    @Excel(name = "问题详细描述")
    private String reasons;
    @Excel(name = "备注")
    private String remark;
}
