package com.land.modular.policy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
@Data
@TableName("sys_policy_info")
public class SysPolicyInfoEntity {
    @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;
    @Basic
    @Column(name = "file_name", nullable = false, length = 100)
    private String fileName;
    @Basic
    @Column(name = "file_suffix", nullable = true, length = 50)
    private String fileSuffix;
    @Basic
    @Column(name = "file_size_kb", nullable = true)
    private Long fileSizeKb;
    @Basic
    @Column(name = "final_name", nullable = false, length = 100)
    private String finalName;
    @Basic
    @Column(name = "file_path", nullable = true, length = 1000)
    private String filePath;
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
    @Column(name = "update_user", nullable = true)
    private Long updateUser;
    @Basic
    @Column(name = "policy_type", nullable = true, length = 100)
    private String policyType;
    @Basic
    @Column(name = "policy_name", nullable = true, length = 200)
    private String policyName;
    @Basic
    @Column(name = "create_user_name", nullable = true, length = 200)
    private String createUserName;
    @Basic
    @Column(name = "update_user_name", nullable = true, length = 200)
    private String updateUserName;

}
