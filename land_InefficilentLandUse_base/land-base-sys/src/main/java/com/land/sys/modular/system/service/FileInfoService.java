package com.land.sys.modular.system.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.sys.core.constant.DefaultAvatar;
import com.land.sys.core.exception.enums.BizExceptionEnum;
import com.land.sys.modular.system.entity.FileInfo;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.mapper.FileInfoMapper;
import com.land.sys.modular.system.model.UploadResult;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件信息表
 * 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
@Slf4j
public class FileInfoService extends ServiceImpl<FileInfoMapper, FileInfo> {

    @Autowired
    private UserService userService;

    /**
     * 更新头像
     *
     * @author fengshuonan
     * @Date 2018/11/10 4:10 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String fileId) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        User user = userService.getById(currentUser.getId());

        //更新用户的头像
        user.setAvatar(fileId);
        userService.updateById(user);
    }

    /**
     * 预览当前用户头像
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:04
     */
    public byte[] previewAvatar() {

        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }

        //获取当前用户的头像id
        User user = userService.getById(currentUser.getId());
        String avatar = user.getAvatar();

        //如果头像id为空就返回默认的
        if (ToolUtil.isEmpty(avatar)) {
            try {
                if(user.getSex().equals("M")){
                    String filePath = "D:\\tudiOA\\img\\man.png";
                    return IoUtil.readBytes(new FileInputStream(filePath));
                }else{
                    String filePath = "D:\\tudiOA\\img\\woman.png";
                    return IoUtil.readBytes(new FileInputStream(filePath));
                }
            } catch (FileNotFoundException e) {
                log.error("头像未找到！", e);
                return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
            }
           // return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
        } else {

            //文件id不为空就查询文件记录
            FileInfo fileInfo = this.getById(avatar);
            if (fileInfo == null) {
                return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
            } else {
                try {
                    String filePath = fileInfo.getFilePath();
                    return IoUtil.readBytes(new FileInputStream(filePath));
                } catch (FileNotFoundException e) {
                    log.error("头像未找到！", e);
                    return Base64.decode(DefaultAvatar.BASE_64_AVATAR);
                }
            }
        }

    }

    /**
     * 上传文件（默认上传路径）
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:18
     */
    public UploadResult uploadFile(String businessKey,MultipartFile file) {
        String fileSavePath = ConstantsContext.getFileUploadPath();
        String nowDate = DateUtil.formatDate(new Date());
        fileSavePath = fileSavePath+nowDate+"/";
        return this.uploadFile(file, fileSavePath,businessKey);
    }

    /**
     * 上传文件（指定上传路径）
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:18
     */
    public UploadResult uploadFile(MultipartFile file, String fileSavePath,String businessKey) {

        UploadResult uploadResult = new UploadResult();

        //生成文件的唯一id
        String fileId = IdWorker.getIdStr();
        uploadResult.setFileId(fileId);

        //获取文件后缀
        String fileSuffix = ToolUtil.getFileSuffix(file.getOriginalFilename());
        uploadResult.setFileSuffix(fileSuffix);

        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        uploadResult.setOriginalFilename(originalFilename);

        //生成文件的最终名称
        String finalName = fileId + "." + ToolUtil.getFileSuffix(originalFilename);
        uploadResult.setFinalName(finalName);
        uploadResult.setFileSavePath(fileSavePath + finalName);

        try {
            //保存文件到指定目录
            File newFile = new File(fileSavePath + finalName);

            //创建父目录
            FileUtil.mkParentDirs(newFile);

            //保存文件
            file.transferTo(newFile);
            Long userId = LoginContextHolder.getContext().getUserId();
            //保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setFileName(originalFilename);
            fileInfo.setFileSuffix(fileSuffix);
            fileInfo.setFilePath(fileSavePath + finalName);
            fileInfo.setFinalName(finalName);
            fileInfo.setBusinessKey(businessKey);
            fileInfo.setCreateUser(userId);

            //计算文件大小kb
            long kb = new BigDecimal(file.getSize())
                    .divide(BigDecimal.valueOf(1024))
                    .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            fileInfo.setFileSizeKb(kb);
            this.save(fileInfo);
        } catch (Exception e) {
            log.error("上传文件错误！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }

        return uploadResult;
    }

    /**
     * 根据文件的最终命名获取文件信息
     *
     * @author fengshuonan
     * @Date 2020/1/1 16:48
     */
    public FileInfo getByFinalName(String finalName) {

        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("final_name", finalName);

        return this.getOne(queryWrapper);
    }

    /**
     * 删除文件
     * @param ids
     * @return
     */
    public boolean deleteFiles(String ids) {
        boolean result = false;
        String[] idList = ids.split(",");
        if(idList != null && idList.length > 0){
            for(String s : idList){
                this.baseMapper.deleteById(s);
            }
            result = true;
        }

        return result;
    }

    /**
     *根据业务key获取文件信息
     * @return
     */
    public List<FileInfo> getListByBusinessKey(String businessKey) {
        return this.baseMapper.getListByBusinessKey(businessKey);
    }
}
