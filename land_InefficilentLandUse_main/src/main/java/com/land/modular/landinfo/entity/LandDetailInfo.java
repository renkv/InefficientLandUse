package com.land.modular.landinfo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
@Data
@TableName("land_detail_info")
public class LandDetailInfo {
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;
    /**
     * 创建人名称
     */
    @TableField(value = "create_user_name")
    private String createUserName;
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private Long updateUser;
    /**
     * 更新新名称
     */
    @TableField(value = "update_user_name")
    private String updateUserName;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    //区县名称
    @TableField(value = "xmc")
    private String xmc;
    //片区编号
    @TableField(value = "pqbh")
    private  String pqbh;
    //项目名称
    @TableField(value = "xmmc")
    private String xmmc;
    //乡镇代码
    @TableField(value = "xzdm")
    private String xzdm;
    //乡镇名称
    @TableField(value = "xzmc")
    private String xzmc;
    //地块编号
    @TableField(value = "dkbh")
    private String dkbh;
    //地块面积
    @TableField(value = "dkmj")
    private Double dkmj;
    //大类代码
    @TableField(value = "dldm")
    private String dldm;
    //大类名称
    @TableField(value = "dlmc")
    private String dlmc;
    //小类代码
    @TableField(value = "xldm")
    private String xldm;
    //小类名称
    @TableField(value = "xlmc")
    private String xlmc;
    //现状用途
    @TableField(value = "xzyt")
    private String xzyt;
    //现状容积率
    @TableField(value = "xzrjl")
    private Double xzrjl;
    //现状建筑密度
    @TableField(value = "xzjzmd")
    private Integer xzjzmd;
    //规划用途
    @TableField(value = "ghyt")
    private String ghyt;
    //规划容积率
    @TableField(value = "ghrjl")
    private String ghrjl;
    //规划建筑密度
    @TableField(value = "ghjzmd")
    private Long ghjzmd;
    //开发潜力
    @TableField(value = "kfql")
    private Double kfql;
    //再开发时序
    @TableField(value = "zkfsx")
    private String zkfsx;
    //片区面积
    @TableField(value = "pqmj")
    private Double pqmj;
    //坐标
    @TableField(value = "zb")
    private String zb;
    //备注
    @TableField(value = "remark")
    private String remark;
    /**
     * 业务key
     */
    @TableField(value = "business_key")
    private String businessKey;
}
