package com.land.modular.file.vo;

import com.land.modular.file.entity.FileReceiveInfo;
import com.land.sys.modular.system.entity.FileInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class FileShareInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Long shareId;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人名称
     */
    private String createUserName;
    /**
     * 主题
     */
    private String shareTitle;
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
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 备注
     */
    private String remark;
    /**
     * 业务key
     */
    private String businessKey;
    /**
     * 接收人id
     */
    private String receiveUser;
    /**
     * 接受人名称
     */
    private String receiveUserName;
    /**
     * 回复内容
     */
    private String reply;
    /**
     * 文件信息
     */
    private List<FileInfo> fileInfoList;
    /**
     * 接收信息
     */
    private List<FileReceiveInfo> fileReceiveInfoList;
    //编号
    private String shareNo;
    //类型
    private String shareType;
}
