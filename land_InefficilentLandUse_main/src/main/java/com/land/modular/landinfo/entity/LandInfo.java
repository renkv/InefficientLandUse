package com.land.modular.landinfo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * 地块信息(LandInfo)实体类
 *
 * @author makejava
 * @since 2021-06-23 16:11:21
 */
public class LandInfo implements Serializable {
    private static final long serialVersionUID = 423854917243938748L;
    /**
     * 唯一标识
     */
    private Long id;
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人姓名
     */
    private String createUserName;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 更新人名称
     */
    private String updateUserName;
    /**
     * 地块类型
     */
    private String landType;
    /**
     * 分类
     */
    private String category;
    /**
     * 权属
     */
    private String ownership;
    /**
     * 规划符合性
     */
    private String planCompliance;
    /**
     * 开发利用强度
     */
    private String useStrength;
    /**
     * 用地效益
     */
    private String benefit;
    /**
     * 土地权利人意愿
     */
    private String desire;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 土地面积
     */
    private String landArea;
    /**
     * 地块名称
     */
    private String landName;
    /**
     * 地块编号
     */
    private String landNo;
    /**
     * 项目编号
     */
    private String landCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 坐标信息
     */
    private String areaInfo;
    /**
     * 坐标文件名称
     */
    private String areaFileName;

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAreaFileName() {
        return areaFileName;
    }

    public void setAreaFileName(String areaFileName) {
        this.areaFileName = areaFileName;
    }

    public String getLandCode() {
        return landCode;
    }

    public void setLandCode(String landCode) {
        this.landCode = landCode;
    }

    public String getLandNo() {
        return landNo;
    }

    public void setLandNo(String landNo) {
        this.landNo = landNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //部门id
    @TableField(value = "dept_id")
    private Long deptId;
    //部门名称
    @TableField(value = "dept_name")
    private String deptName;

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

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getPlanCompliance() {
        return planCompliance;
    }

    public void setPlanCompliance(String planCompliance) {
        this.planCompliance = planCompliance;
    }

    public String getUseStrength() {
        return useStrength;
    }

    public void setUseStrength(String useStrength) {
        this.useStrength = useStrength;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getDesire() {
        return desire;
    }

    public void setDesire(String desire) {
        this.desire = desire;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

}
