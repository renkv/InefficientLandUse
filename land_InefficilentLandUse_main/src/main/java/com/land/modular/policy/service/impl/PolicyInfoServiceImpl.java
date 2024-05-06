package com.land.modular.policy.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import cn.stylefeng.roses.kernel.model.response.ErrorResponseData;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.policy.entity.SysPolicyInfoEntity;
import com.land.modular.policy.mapper.PolicyDao;
import com.land.modular.policy.service.PolicyInfoService;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import com.land.sys.modular.system.entity.FileInfo;
import com.land.utils.AreaUtil;
import com.land.utils.DoubleUtils;
import com.land.utils.Tuple;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * 方法实现类
 */
@Service("policyInfoService")
public class PolicyInfoServiceImpl extends ServiceImpl<PolicyDao, SysPolicyInfoEntity> implements PolicyInfoService {
    /**
     * 分页查询
     * @param vo
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Map<String, Object>> selectList(SysPolicyInfoVo vo, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        //vo.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, vo, beginTime, endTime);
    }

    /**
     * 保存政策信息
     * @param file
     * @param vo
     * @return
     */
    @Override
    public ResponseData savePolicy(MultipartFile file, SysPolicyInfoVo vo) {
        SysPolicyInfoEntity entity = new SysPolicyInfoEntity();
        if(!file.isEmpty()){
            //获取文件后缀
            String fileSuffix = ToolUtil.getFileSuffix(file.getOriginalFilename());
            String fileSavePath = ConstantsContext.getFileUploadPath();
            String nowDate = DateUtil.formatDate(new Date());
            fileSavePath = fileSavePath+nowDate+"/";

            //获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            //生成文件的唯一id
            String fileId = IdWorker.getIdStr();
            //生成文件的最终名称
            String finalName = fileId + "." + fileSuffix;
            try {
                //保存文件到指定目录
                File newFile = new File(fileSavePath + finalName);
                //创建父目录
                FileUtil.mkParentDirs(newFile);
                //保存文件
                file.transferTo(newFile);
                LoginUser currentUser = LoginContextHolder.getContext().getUser();
                //保存文件信息
                entity.setFileId(fileId);
                entity.setFileName(originalFilename);
                entity.setFileSuffix(fileSuffix);
                entity.setFilePath(fileSavePath + finalName);
                entity.setFinalName(finalName);
                entity.setCreateUser(currentUser.getId());
                entity.setCreateUserName(currentUser.getName());
                entity.setCreateTime(new Date());
                entity.setPolicyName(vo.getPolicyName());
                entity.setPolicyType(vo.getPolicyType());
                //计算文件大小kb
                long kb = new BigDecimal(file.getSize())
                        .divide(BigDecimal.valueOf(1024))
                        .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                entity.setFileSizeKb(kb);
                this.baseMapper.insert(entity);
            } catch (Exception e) {
                System.out.println("读取文件内容出错");
                e.printStackTrace();
                return new ErrorResponseData("读取文件内容出错");
            }
        }
        return new SuccessResponseData();
    }

    /**
     * 删除政策信息
     * @param ids
     * @return
     */

    @Override
    public boolean delete(String ids) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        String[] list = ids.split(",");
        for(String s : list){
            this.baseMapper.deleteById(s);
        }
        return true;
    }
}
