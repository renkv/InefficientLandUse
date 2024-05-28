package com.land.modular.landinfo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.sql.Timestamp;
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
    //低效用地类型
    @TableField(value = "category")
    private String category;
    /**
     * 业务key
     */
    @TableField(value = "business_key")
    private String businessKey;
    /**
     * 编码
     */
    @TableField(value = "land_code")
    private String landCode;
    /**
     * 状态
     */
    @TableField(value = "land_status")
    private String landStatus;
    /**
     * 规划规模面积
     */
    @TableField(value = "ghgmmj")
    private Double ghgmmj;
    /**
     * 规划规模比例
     */
    @TableField(value = "ghgmbl")
    private Double ghgmbl;
    /**
     * 容积率
     */
    @TableField(value = "rjl")
    private Double rjl;
    /**
     * 现状规模面积
     */
    @TableField(value = "xzgmmj")
    private Double xzgmmj;
    /**
     * 现状规模比例
     */
    @TableField(value = "xzgmbl")
    private Double xzgmbl;
    /**
     * 可实现潜力
     */
    @TableField(value = "ksxql")
    private Double ksxql;
    /**
     * 现状面积
     */
    @TableField(value = "xzmj")
    private Double xzmj;
    /**
     * 现状比例
     */
    @TableField(value = "xzbl")
    private Double xzbl;
    /**
     * 现状地均固定资产投资
     */
    @TableField(value = "xzdjgdzctz")
    private Double xzdjgdzctz;
    /**
     * 现状单位GDP产出
     */
    @TableField(value = "xzdwGDPcc")
    private Double xzdwgdpPcc;
    /**
     * 规划地均固定资产投资
     */
    @TableField(value = "ghdjgdzctz")
    private Double ghdjgdzctz;
    /**
     * 规划单位GDP产出
     */
    @TableField(value = "ghdwgdpcc")
    private Double ghdwgdpcc;
    /**
     * 规划面积
     */
    @TableField(value = "ghmj")
    private Double ghmj;
    /**
     * 规划比例
     */
    @TableField(value = "ghbl")
    private Double ghbl;
    @TableField(value = "zbq")
    private String zbq;
    @Basic
    @TableField(value = "qymc")
    private String qymc;
    @Basic
    @TableField(value = "bdcqzsh")
    private String bdcqzsh;
    @Basic
    @TableField(value = "crsj")
    private Timestamp crsj;
    @Basic
    @TableField(value = "htrjl")
    private String htrjl;
    @Basic
    @TableField(value = "httzqd")
    private String httzqd;
    @Basic
    @TableField(value = "htmjss")
    private String htmjss;
    @Basic
    @TableField(value = "hydm")
    private String hydm;
    @Basic
    @TableField(value = "hymc")
    private String hymc;
    @Basic
    @TableField(value = "rjljsz")
    private Double rjljsz;
    @Basic
    @TableField(value = "dkrjl")
    private String dkrjl;
    @Basic
    @TableField(value = "rjlfz")
    private Double rjlfz;
    @Basic
    @TableField(value = "tzqdjs")
    private Double tzqdjs;
    @Basic
    @TableField(value = "dktzqd")
    private String dktzqd;
    @Basic
    @TableField(value = "tzqdfz")
    private Double tzqdfz;
    @Basic
    @TableField(value = "ssccljs")
    private Double ssccljs;
    @Basic
    @TableField(value = "dkssccl")
    private String dkssccl;
    @Basic
    @TableField(value = "dksscclfz")
    private Double dksscclfz;
    @Basic
    @TableField(value = "bmysfz")
    private Double bmysfz;
    @Basic
    @TableField(value = "zfysfz")
    private Double zfysfz;
    @Basic
    @TableField(value = "zhdf")
    private Double zhdf;
    @Basic
    @TableField(value = "xdm")
    private String xdm;
    @Basic
    @TableField(value = "year")
    private Integer year;

    @Basic
    @Column(name = "cbhs", nullable = true, length = 20)
    private String cbhs;
    @Basic
    @Column(name = "xyqd", nullable = true, length = 20)
    private String xyqd;
    @Basic
    @Column(name = "tdgy", nullable = true, length = 20)
    private String tdgy;
    @Basic
    @Column(name = "ghtjbg", nullable = true, length = 20)
    private String ghtjbg;
    @Basic
    @Column(name = "ghgcsp", nullable = true, length = 20)
    private String ghgcsp;
    @Basic
    @Column(name = "cxrd", nullable = true, length = 20)
    private String cxrd;
    @Basic
    @Column(name = "tsfabz", nullable = true, length = 20)
    private String tsfabz;
    @Basic
    @Column(name = "tsfzss", nullable = true, length = 20)
    private String tsfzss;
    @Basic
    @Column(name = "tscxzm", nullable = true, length = 20)
    private String tscxzm;
    @Basic
    @Column(name = "xmlx", nullable = true, length = 20)
    private String xmlx;
    @Basic
    @Column(name = "xmsg", nullable = true, length = 20)
    private String xmsg;
    @Basic
    @Column(name = "xmys", nullable = true, length = 20)
    private String xmys;
    @Basic
    @Column(name = "xmzbsy", nullable = true, length = 20)
    private String xmzbsy;
    @Basic
    @Column(name = "jbqk", nullable = true, length = 20)
    private String jbqk;
    @Basic
    @Column(name = "dktj", nullable = true, length = 20)
    private String dktj;
    @Basic
    @Column(name = "sczkfjz", nullable = true, length = 1000)
    private String sczkfjz;
    @Basic
    @Column(name = "sc_status", nullable = true, length = 20)
    private String scStatus;

    /**
     *是否实施 1是0否
     */
    @Basic
    @Column(name = "sfss", nullable = true, length = 10)
    private String sfss;
    /**
     * 未实施原因 1未到计划的再开发时序 2企业原因导致无法实施 3政府原因导致无法实施
     */
    @Basic
    @Column(name = "wssyy", nullable = true, length = 10)
    private String wssyy;
    /**
     * 未实施具体原因描述
     */
    @Basic
    @Column(name = "tjyy", nullable = true, length = 2000)
    private String tjyy;
    /**
     *是否正在实施 1是 0否
     */
    @Basic
    @Column(name = "zzss", nullable = true, length = 10)
    private String zzss;
    /**
     *开始时间
     */
    @Basic
    @Column(name = "kssj", nullable = true, length = 50)
    private String kssj;
    /**
     *是否已完成 1是 0否
     */
    @Basic
    @Column(name = "ywc", nullable = true, length = 10)
    private String ywc;

    /**
     *完成时间
     */
    @Basic
    @Column(name = "wcsj", nullable = true, length = 50)
    private String wcsj;

}
