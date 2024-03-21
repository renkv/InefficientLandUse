package com.land.modular.file.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 *文件分享信息
 */
@TableName("file_share_info")
public class FileShareInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "share_id", type = IdType.ID_WORKER)
    private Long shareId;
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
     * 主题
     */
    @TableField(value = "share_title")
    private String shareTitle;
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
    /**
     * 是否删除
     */
    @TableField("is_delete")
    private String isDelete;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 业务key
     */
    @TableField(value = "business_key")
    private String businessKey;
    /**
     * 接收人id
     */
    @TableField(value = "receive_user")
    private String receiveUser;
    /**
     * 接受人名称
     */
    @TableField(value = "receive_user_name")
    private String receiveUserName;
    //编号
    @TableField(value = "share_no")
    private String shareNo;
    //类型
    @TableField(value = "share_type")
    private String shareType;

    public String getShareNo() {
        return shareNo;
    }

    public void setShareNo(String shareNo) {
        this.shareNo = shareNo;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
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

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
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

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
