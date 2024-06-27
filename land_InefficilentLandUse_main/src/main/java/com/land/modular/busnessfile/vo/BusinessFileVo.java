package com.land.modular.busnessfile.vo;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class BusinessFileVo {
    private String id;

    private String fileName;

    private String fileSuffix;

    private Long fileSizeKb;

    private String finalName;

    private String filePath;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Long createUser;

    private Long updateUser;

    private String countyCode;

    private String countyName;

    private String fileYear;

    private String updateUserName;

    private String createUserName;
}
