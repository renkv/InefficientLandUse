package com.land.modular.landinfo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.land.sys.modular.system.entity.FileInfo;
import lombok.Data;

import javax.persistence.Basic;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class LandDetailInfoVo {
    private Long id;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人名称
     */
    private String createUserName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 更新新名称
     */
    private String updateUserName;
    /**
     * 更新时间
     */
    private Date updateTime;
    //区县名称
    private String xmc;
    //片区编号
    private  String pqbh;
    //项目名称
    private String xmmc;
    //乡镇代码
    private String xzdm;
    //乡镇名称
    private String xzmc;
    //地块编号
    private String dkbh;
    //地块面积
    private Double dkmj;
    //大类代码
    private String dldm;
    //大类名称
    private String dlmc;
    //小类代码
    private String xldm;
    //小类名称
    private String xlmc;
    //现状用途
    private String xzyt;
    //现状容积率
    private Double xzrjl;
    //现状建筑密度
    private Integer xzjzmd;
    //规划用途
    private String ghyt;
    //规划容积率
    private String ghrjl;
    //规划建筑密度
    private Long ghjzmd;
    //开发潜力
    private Double kfql;
    //再开发时序
    private String zkfsx;
    //片区面积
    private Double pqmj;
    private String zb;
    private String remark;
    private String businessKey;
    //低效用地类型
    private String category;
    @TableField(value = "land_code")
    private String landCode;
    /**
     * 状态
     */
    @TableField(value = "land_status")
    private String landStatus;
    /**
     * 规模面积
     */
    private Double gmmj;
    /**
     * 规模比例
     */
    private Double gmbl;
    /**
     * 容积率
     */
    private Double rjl;
    /**
     * 现状规模面积
     */
    private Double xzgmmj;
    /**
     * 现状规模比例
     */
    private Double xzgmbl;
    /**
     * 可实现潜力
     */
    private Double ksxql;
    /**
     * 现状面积
     */
    private Double xzmj;
    /**
     * 现状比例
     */
    private Double xzbl;
    /**
     * 现状地均固定资产投资
     */
    private Double xzdjgdzctz;
    /**
     * 现状单位GDP产出
     */
    private Double xzdwgdpPcc;
    /**
     * 规划面积
     */
    private Double ghmj;
    /**
     * 规划比例
     */
    private Double ghbl;
    private String zbq;

    private String qymc;

    private String bdcqzsh;

    private Timestamp crsj;

    private String htrjl;

    private String httzqd;

    private String htmjss;

    private String hydm;

    private String hymc;

    private Double rjljsz;

    private String dkrjl;

    private Double rjlfz;

    private Double tzqdjs;

    private String dktzqd;

    private Double tzqdfz;

    private Double ssccljs;

    private String dkssccl;

    private Double dksscclfz;

    private Double bmysfz;

    private Double zfysfz;

    private Double zhdf;

    private String xdm;
    /**
     * 文件信息
     */
    private List<FileInfo> fileInfoList;
}
