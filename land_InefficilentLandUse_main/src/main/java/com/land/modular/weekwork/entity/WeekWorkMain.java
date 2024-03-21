package com.land.modular.weekwork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mcrkw
 * @date 2021/4/19 15:09
 * 周工作报告主表
 */
@TableName("week_work_main")
public class WeekWorkMain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
   //创建人id
    @TableField(value = "create_user")
    private Long createUser;
    //创建人名称
    @TableField(value = "create_user_name")
    private String createUserName;
    //部门id
    @TableField(value = "dept_id")
    private Long deptId;
    //部门名称
    @TableField(value = "dept_name")
    private String deptName;
    //年份
    @TableField(value = "year")
    private String year;
    //月份
    @TableField(value = "month")
    private String month;
    //周
    @TableField(value = "week")
    private String week;
    //创建时间
    @TableField(value = "create_time")
    private Date createTime;
    //更新人id
    @TableField(value = "update_user")
    private Long updateUser;
    //更新人名称
    @TableField(value = "update_user_name")
    private String updateUserName;
    //更新时间
    @TableField(value = "update_time")
    private Date updateTime;
    //状态
    @TableField(value = "status")
    private String status;
    //备注
    @TableField(value = "remark")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
