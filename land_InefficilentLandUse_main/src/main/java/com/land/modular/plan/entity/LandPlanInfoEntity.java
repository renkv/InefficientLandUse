package com.land.modular.plan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
@Data
@TableName("land_plan_info")
public class LandPlanInfoEntity {
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    @Basic
    @Column(name = "land_code", nullable = false, length = 100)
    private String landCode;
    @Basic
    @Column(name = "plan_type", nullable = true, length = 50)
    private String planType;
    @Basic
    @Column(name = "plan_name", nullable = true, length = 50)
    private String planName;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Date createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Date updateTime;
    @Basic
    @Column(name = "create_user", nullable = true)
    private Long createUser;
    @Basic
    @Column(name = "create_user_name", nullable = true)
    private String  createUserName;
    @Basic
    @Column(name = "update_user", nullable = true)
    private Long updateUser;
    @Basic
    @Column(name = "update_user_name", nullable = true)
    private String  updateUserName;
    @Basic
    @Column(name = "plan_start_time", nullable = true, length = 20)
    private String planStartTime;
    @Basic
    @Column(name = "plan_end_time", nullable = true, length = 20)
    private String planEndTime;
    @Basic
    @Column(name = "act_start_time", nullable = true, length = 20)
    private String actStartTime;
    @Basic
    @Column(name = "act_end_time", nullable = true, length = 20)
    private String actEndTime;
    @Basic
    @Column(name = "plan_area", nullable = true, precision = 0)
    private Double planArea;
    @Basic
    @Column(name = "current_area", nullable = true, precision = 0)
    private Double currentArea;
    @Basic
    @Column(name = "rem_area", nullable = true, precision = 0)
    private Double remArea;
    @Basic
    @Column(name = "cur_status", nullable = true, length = 10)
    private String curStatus;
    @Basic
    @Column(name = "cur_progress", nullable = true, length = 1000)
    private String curProgress;
    @Basic
    @Column(name = "reasons", nullable = true, length = 1000)
    private String reasons;
    @Basic
    @Column(name = "reasons_type", nullable = true, length = 50)
    private String reasonsType;
    @Basic
    @Column(name = "village_name", nullable = true, length = 50)
    private String villageName;
    @Basic
    @Column(name = "renewal_name", nullable = true, length = 50)
    private String renewalName;
    @Basic
    @Column(name = "zone_name", nullable = true, length = 50)
    private String zoneName;
    @Basic
    @Column(name = "plan_unit", nullable = true, length = 50)
    private String planUnit;
    @Basic
    @Column(name = "act_unit", nullable = true, length = 50)
    private String actUnit;
    @Basic
    @Column(name = "credit_code", nullable = true, length = 50)
    private String creditCode;
    @Basic
    @Column(name = "bus_name", nullable = true, length = 50)
    private String busName;
    @Basic
    @Column(name = "occupy_area", nullable = true, precision = 0)
    private Double occupyArea;
    @Basic
    @Column(name = "located", nullable = true, length = 200)
    private String located;
    @Basic
    @Column(name = "pre_year_tax", nullable = true, precision = 0)
    private Double preYearTax;
    @Basic
    @Column(name = "up_year_tax", nullable = true, precision = 0)
    private Double upYearTax;
    @Basic
    @Column(name = "is_plan_bus", nullable = true, length = 20)
    private String isPlanBus;
    @Basic
    @Column(name = "bus_status", nullable = true, length = 20)
    private String busStatus;
    @Basic
    @Column(name = "remark", nullable = true, length = 2000)
    private String remark;

    @Basic
    @Column(name = "plan_code", nullable = true, length = 2000)
    private String planCode;

}
