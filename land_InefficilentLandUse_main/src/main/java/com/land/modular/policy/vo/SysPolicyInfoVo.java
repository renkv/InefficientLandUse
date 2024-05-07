package com.land.modular.policy.vo;

import lombok.Data;
import java.util.Date;

@Data
public class SysPolicyInfoVo {
    private String fileId;
    private String fileName;
    private String fileSuffix;
    private Long fileSizeKb;
    private String finalName;
    private String filePath;
    private Date createTime;
    private Date updateTime;
    private Long createUser;
    private Long updateUser;
    private String policyType;
    private String policyName;
    private String createUserName;
    private String updateUserName;
    private String policyTypeName;
}
