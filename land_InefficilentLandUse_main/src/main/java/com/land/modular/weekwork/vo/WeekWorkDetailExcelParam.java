package com.land.modular.weekwork.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mcrkw
 * @date 2021年04月28日 16:29
 */
@Data
public class WeekWorkDetailExcelParam implements Serializable {
    //序号
    @Excel(name = "序号")
    private Integer serialNo;
    //任务名称
    @Excel(name = "任务名称")
    private String taskName;
    //项目落实人
    @Excel(name = "项目落实人")
    private String implementUser;
    //任务下达时间
    @Excel(name = "任务下达时间")
    private String assignTime;
    //计划完成时间
    @Excel(name = "计划完成时间")
    private String planFinishTime;
    //实际进度
    @Excel(name = "实际进度")
    private String actualProgress;
    //主管领导
    @Excel(name = "签字")
    private String leader;
    //督导情况
    @Excel(name = "督导情况")
    private String supervision;
    //督导时间
    @Excel(name = "督导时间")
    private String superviseTime;
    //备注
    @Excel(name = "备注")
    private String remark;
    //状态 0 进行中 1 暂停 2 跟进 3 完成 4 计划跟进
    @Excel(name = "项目状态")
    private String status;

    @Excel(name = "领导批示")
    private String instruction;
}
