package com.land.modular.busnessfile.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import cn.stylefeng.roses.kernel.model.response.ErrorResponseData;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.busnessfile.entity.BusinessFileInfoEntity;
import com.land.modular.busnessfile.mapper.BusinessFileDao;
import com.land.modular.busnessfile.service.BusinessFileService;
import com.land.modular.busnessfile.vo.BusinessFileVo;
import com.land.modular.policy.entity.SysPolicyInfoEntity;
import com.land.sys.modular.system.entity.Dict;
import com.land.sys.modular.system.service.DictService;
import com.land.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("BusinessFileService")
public class BusinessFileServiceImpl  extends ServiceImpl<BusinessFileDao, BusinessFileInfoEntity> implements BusinessFileService {
    @Autowired
    private DictService dictService;
    /**
     * 分页查询数据
     * @param vo
     * @return
     */
    @Override
    public Page<Map<String, Object>> selectList(BusinessFileVo vo) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if(currentUser.getDeptName() != null && !(currentUser.getDeptName().equals("石家庄市自然资源与规划局"))){
            vo.setCountyName(currentUser.getDeptName());
        }
        return this.baseMapper.selectListByPage(page, vo);
    }

    /**
     * 查询所有的年份
     * @return
     */
    @Override
    public List<Map<String, Object>> getDistinctYear() {
        return this.baseMapper.getDistinctYear();
    }

    /***
     * 保存文件信息
     * @param file
     * @param vo
     * @return
     */
    @Override
    public ResponseData saveFieInfo(MultipartFile file, BusinessFileVo vo) {
        BusinessFileInfoEntity entity = new BusinessFileInfoEntity();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        if(vo.getId() != null){
            entity = this.baseMapper.selectById(vo.getId());
            entity.setUpdateUser(currentUser.getId());
            entity.setUpdateUserName(currentUser.getName());
            entity.setUpdateTime(new Date());
        }else{
            entity.setCreateUser(currentUser.getId());
            entity.setCreateUserName(currentUser.getName());
            entity.setCreateTime(new Date());
            //生成文件的唯一id
            String fileId = IdWorker.getIdStr();
            entity.setId(fileId);
        }
        BeanCopyUtils.copyNotNullProperties(vo,entity);
        //获取当前人所属区县
        String deptName = currentUser.getDeptName();
        String countyName = "";
        String coutryCode = "";
        if(StringUtils.isNotBlank(deptName)){
           if(deptName.contains(",")){
               String[] deptNameList = deptName.split(",");
               for(String s : deptNameList) {
                   Dict deptDict = dictService.getOneByNameAndCode(s,"sjzqx");
                   if(deptDict != null){
                       if(countyName.length() > 0){
                           countyName = countyName + "," + deptDict.getName();
                           coutryCode = coutryCode + "," + deptDict.getCode();
                       }else{
                           countyName = deptDict.getName();
                           coutryCode = deptDict.getCode();
                       }
                   }
               }
           }else{
               Dict deptDict = dictService.getOneByNameAndCode(deptName,"sjzqx");
               if(deptDict != null){
                   countyName = deptDict.getName();
                   coutryCode = deptDict.getCode();
               }
           }
        }
        entity.setCountyName(countyName);
        entity.setCountyCode(coutryCode);


        if(!file.isEmpty()){
            //获取文件后缀
            String fileSuffix = ToolUtil.getFileSuffix(file.getOriginalFilename());
            String fileSavePath = ConstantsContext.getFileUploadPath();
            String nowDate = DateUtil.formatDate(new Date());
            fileSavePath = fileSavePath+nowDate+"\\";

            //获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            String filename = IdWorker.getIdStr();
            //生成文件的最终名称
            String finalName = filename+ "." + fileSuffix;
            try {
                //保存文件到指定目录
                File newFile = new File(fileSavePath + finalName);
                //创建父目录
                FileUtil.mkParentDirs(newFile);
                //保存文件
                file.transferTo(newFile);
                //保存文件信息
                entity.setFileName(originalFilename);
                entity.setFileSuffix(fileSuffix);
                entity.setFilePath(fileSavePath + finalName);
                entity.setFinalName(finalName);
                //计算文件大小kb
                long kb = new BigDecimal(file.getSize())
                        .divide(BigDecimal.valueOf(1024))
                        .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                entity.setFileSizeKb(kb);
            } catch (Exception e) {
                System.out.println("读取文件内容出错");
                e.printStackTrace();
                return new ErrorResponseData("读取文件内容出错");
            }
        }
        if(vo.getId() == null){
            this.baseMapper.insert(entity);
        }else{
            this.baseMapper.updateById(entity);
        }
        return new SuccessResponseData();
    }

    /**
     * 删除
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

    /**
     * 根据id查询实体
     * @param id
     * @return
     */
    @Override
    public BusinessFileInfoEntity selectById(String id) {
        BusinessFileInfoEntity entity = this.baseMapper.selectById(id);
        return entity;
    }
}
