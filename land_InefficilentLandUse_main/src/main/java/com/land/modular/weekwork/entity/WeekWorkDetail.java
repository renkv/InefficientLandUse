package com.land.modular.weekwork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mcrkw
 * @date 2021年04月19日 15:17
 * 周工作报告明细表
 */
@TableName("week_work_detail")
public class WeekWorkDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    //创建时间
    @TableField(value = "create_time")
    private Date createTime;
    //创建人
    @TableField(value = "create_user")
    private Long createUser;
    //创建人名称
    @TableField(value = "create_user_name")
    private String createUserName;
    //更新时间
    @TableField(value = "update_time")
    private Date updateTime;
    //更新人
    @TableField(value = "update_user")
    private Long updateUser;
    //更新人名称
    @TableField(value = "update_user_name")
    private String updateUserName;
    //序号
    @TableField(value = "serial_no")
    private Integer serialNo;
    //任务名称
    @TableField(value = "task_name")
    private String taskName;
    //项目落实人
    @TableField(value = "implement_user")
    private String implementUser;
    //任务下达时间
    @TableField(value = "assign_time")
    private String assignTime;
    //计划完成时间
    @TableField(value = "plan_finish_time")
    private String planFinishTime;
    //实际进度
    @TableField(value = "actual_progress")
    private String actualProgress;
    //主管领导
    @TableField(value = "leader")
    private String leader;
    //督导情况
    @TableField(value = "supervision")
    private String supervision;
    //督导时间
    @TableField(value = "supervise_time")
    private String superviseTime;
    //备注
    @TableField(value = "remark")
    private String remark;
    //状态 0 进行中 1 暂停 2 跟进 3 完成 4 计划跟进 5 培训 6 会议
    @TableField(value = "status")
    private String status;
    //主表id
    @TableField(value = "main_id")
    private Long mainId;
    //领导批示
    @TableField(value = "instruction")
    private String instruction;

    @TableField(value = "instruction_time")
    private Date instructionTime;

    public Date getInstructionTime() {
        return instructionTime;
    }

    public void setInstructionTime(Date instructionTime) {
        this.instructionTime = instructionTime;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getImplementUser() {
        return implementUser;
    }

    public void setImplementUser(String implementUser) {
        this.implementUser = implementUser;
    }

    public String getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    public String getPlanFinishTime() {
        return planFinishTime;
    }

    public void setPlanFinishTime(String planFinishTime) {
        this.planFinishTime = planFinishTime;
    }

    public String getActualProgress() {
        return actualProgress;
    }

    public void setActualProgress(String actualProgress) {
        this.actualProgress = actualProgress;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getSupervision() {
        return supervision;
    }

    public void setSupervision(String supervision) {
        this.supervision = supervision;
    }

    public String getSuperviseTime() {
        return superviseTime;
    }

    public void setSuperviseTime(String superviseTime) {
        this.superviseTime = superviseTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }
}
