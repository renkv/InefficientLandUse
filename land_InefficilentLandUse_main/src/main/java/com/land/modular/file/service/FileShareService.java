package com.land.modular.file.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.entity.FileReceiveInfo;
import com.land.modular.file.entity.FileShareInfo;
import com.land.modular.file.mapper.FileShareInfoMapper;
import com.land.modular.file.vo.FileShareInfoVo;
import com.land.sys.core.exception.enums.BizExceptionEnum;
import com.land.sys.modular.system.entity.FileInfo;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.service.FileInfoService;
import com.land.sys.modular.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FileShareService extends ServiceImpl<FileShareInfoMapper, FileShareInfo> {
    @Autowired
    private UserService userService;
    @Autowired
    private FileReceiveService fileReceiveService;
    @Autowired
    private FileInfoService fileInfoService;
    /**
     * 新增发送信息
     * @param fileShareInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFileShare(FileShareInfo fileShareInfo) {
        if (ToolUtil.isOneEmpty(fileShareInfo, fileShareInfo.getBusinessKey(), fileShareInfo.getShareTitle(),fileShareInfo.getReceiveUser())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        User user = userService.getById(currentUser.getId());

        fileShareInfo.setIsDelete("0");
        if(StringUtils.isEmpty(fileShareInfo.getShareId())){
            fileShareInfo.setCreateUser(currentUser.getId());
            fileShareInfo.setCreateUserName(user.getName());
        }else{
            fileShareInfo.setUpdateUser(currentUser.getId());
            fileShareInfo.setUpdateUserName(user.getName());
            fileReceiveService.deleteByShareId(fileShareInfo.getShareId());
        }
        this.saveOrUpdate(fileShareInfo);

        String[] receiveIds = fileShareInfo.getReceiveUser().split(",");
        String[] receiveNames = fileShareInfo.getReceiveUserName().split(",");
        for(int i=0;i<receiveIds.length;i++){
            String id = receiveIds[i];
            String name = receiveNames[i];
            FileReceiveInfo receiveInfo = new FileReceiveInfo();
            receiveInfo.setIsDelete("0");
            receiveInfo.setIsRead("0");
            receiveInfo.setIsReply("0");
            receiveInfo.setReceiveUser(Long.valueOf(id));
            receiveInfo.setReceiveUserName(name);
            receiveInfo.setShareId(fileShareInfo.getShareId());
            receiveInfo.setCreateTime(new Date());
            receiveInfo.setCreateUser(fileShareInfo.getCreateUserName());
            fileReceiveService.saveOrUpdate(receiveInfo);
        }
    }

    /**
     * 查询列表信息
     * @param info
     * @param beginTime
     * @param endTime
     * @return
     */
    public Page<Map<String, Object>> selectShareList(FileShareInfo info, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        return this.baseMapper.selectShareList(page, info, beginTime, endTime,currentUser.getId());
    }

    /**
     * 获取分享详情
     * @param shareId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FileShareInfoVo getDetailById(Long shareId) {
        FileShareInfoVo shareInfoVo = this.baseMapper.getDetailById(shareId);
        List<FileInfo> fileInfoList = fileInfoService.getListByBusinessKey(shareInfoVo.getBusinessKey());
        List<FileReceiveInfo> fileReceiveInfoList = fileReceiveService.getListByShareId(shareId);
        String[] userIds = shareInfoVo.getReceiveUser().split(",");
        StringBuilder sb = new StringBuilder();
        for(String s :userIds){
            fileReceiveInfoList.forEach(fileReceiveInfo -> {
                sb.append(fileReceiveInfo.getReceiveUserName());
                if("1".equals(fileReceiveInfo.getIsReply())){
                    sb.append("(已读),");
                }else{
                    sb.append("(未读),");
                }
            });
        }
        shareInfoVo.setReceiveUserName(sb.toString().substring(0,sb.toString().length()-1));
        shareInfoVo.setFileInfoList(fileInfoList);
        shareInfoVo.setFileReceiveInfoList(fileReceiveInfoList);
        return shareInfoVo;
    }

    /**
     *
     * @param shareId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long shareId) {
        FileShareInfo info = this.baseMapper.selectById(shareId);
        info.setIsDelete("1");
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        User user = userService.getById(currentUser.getId());
        info.setUpdateUser(currentUser.getId());
        info.setUpdateUserName(user.getName());
        this.saveOrUpdate(info);
        fileReceiveService.updateByShareId(shareId);
    }

    /**
     * 保存转发信息
     * @param fileShareInfo
     * @param oldShareId
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFileForward(FileShareInfo fileShareInfo, String oldShareId) {
        this.saveFileShare(fileShareInfo);
        FileShareInfo info = this.baseMapper.selectById(oldShareId);
        List<FileInfo> fileInfoList = fileInfoService.getListByBusinessKey(info.getBusinessKey());
        fileInfoList.forEach(item->{
            //生成文件的唯一id
            String fileId = IdWorker.getIdStr();
            //保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(item.getFileName());
            fileInfo.setFileSuffix(item.getFileSuffix());
            fileInfo.setFilePath(item.getFilePath());
            fileInfo.setFinalName(item.getFinalName());
            fileInfo.setBusinessKey(fileShareInfo.getBusinessKey());
            fileInfo.setCreateUser(info.getCreateUser());
            fileInfo.setFileSizeKb(item.getFileSizeKb());
            fileInfoService.save(fileInfo);
        });

    }
}
