package com.land.modular.landinfo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@Data
public class LandDetailExcelParam implements Serializable {
    //区县名称
    @Excel(name = "县名称",orderNum = "10")
    @NotEmpty(message = "县名称不能为空")
    private String xmc;
    //片区编号
   /* @Excel(name = "片区编号")
    private  String pqbh;*/
    //项目名称
    @Excel(name = "项目名称",orderNum = "15")
    @NotEmpty(message = "项目名称不能为空")
    private String xmmc;
    //乡镇代码
  /*  @Excel(name = "乡镇代码")
    private String xzdm;
    //乡镇名称
    @Excel(name = "乡镇名称")
    private String xzmc;*/
    //地块编号
/*    @Excel(name = "地块编号")
    private String dkbh;*/
    //地块面积
    @Excel(name = "地块面积",orderNum = "20")
    @NotEmpty(message = "地块面积不能为空")
    private Double dkmj;
    //大类代码
/*    @Excel(name = "大类代码")
    private String dldm;*/
    //大类名称
    @Excel(name = "大类名称",orderNum = "25")
    private String dlmc;
    //小类代码
/*    @Excel(name = "小类代码")
    private String xldm;*/
    //小类名称
    @Excel(name = "小类名称",orderNum = "30")
    private String xlmc;
    //现状用途
    @Excel(name = "现状用途",orderNum = "35")
    private String xzyt;
    //现状容积率
    @Excel(name = "现状容积率",orderNum = "40")
    private Double xzrjl;
    //现状建筑密度
    @Excel(name = "现状建筑密度",orderNum = "45")
    private Integer xzjzmd;
    //规划用途
    @Excel(name = "规划用途",orderNum = "50")
    private String ghyt;
    //规划容积率
    @Excel(name = "规划容积率",orderNum = "55")
    private String ghrjl;
    //规划建筑密度
    @Excel(name = "规划建筑密度",orderNum = "60")
    private Long ghjzmd;
    //开发潜力
    @Excel(name = "开发潜力",orderNum = "65")
    private Double kfql;
    //再开发时序
    @Excel(name = "再开发时序",orderNum = "70")
    private String zkfsx;
    //片区面积
    @Excel(name = "片区面积",orderNum = "75")
    private Double pqmj;
}
