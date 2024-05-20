package com.land.modular.plan.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.vo.LandDetailExcelParam;
import com.land.modular.plan.entity.LandPlanInfoEntity;
import com.land.modular.plan.mapper.LandPlanDao;
import com.land.modular.plan.service.LandPlanInfoService;
import com.land.modular.plan.vo.LandPlanExcelParam;
import com.land.modular.plan.vo.LandPlanInfoVo;

import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.service.UserService;
import com.land.utils.BeanCopyUtils;
import com.land.utils.PinyinUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("landPlanInfoService")
public class LandPlanInfoServiceImpl extends ServiceImpl<LandPlanDao, LandPlanInfoEntity> implements LandPlanInfoService {
    @Autowired
    private UserService userService;
    /**
     * 分页查询列表
     * @param vo
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Map<String, Object>> selectList(LandPlanInfoVo vo, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        //vo.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, vo, beginTime, endTime);
    }

    /**
     * 保存实施计划
     * @param vo
     * @return
     */
    @Override
    public ResponseData savePlan(LandPlanInfoVo vo) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        User user = userService.getById(currentUser.getId());
        LandPlanInfoEntity entity = new LandPlanInfoEntity();
        if(StringUtils.isEmpty(vo.getId())){
            //时间（精确到毫秒）
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            //3位随机数
            String randomNumeric = RandomStringUtils.randomNumeric(3);
            String planCode = "P"+localDate + randomNumeric;
            BeanUtils.copyProperties(vo,entity);
            entity.setPlanCode(planCode);
            entity.setCreateUser(currentUser.getId());
            entity.setCreateUserName(user.getName());
            entity.setCreateTime(new Date());
            if(entity.getPlanArea() != null && entity.getCurrentArea() != null){
                entity.setRemArea(entity.getPlanArea()-entity.getCurrentArea());
            }
        }else{
            entity = this.baseMapper.selectById(vo.getId());
            BeanCopyUtils.copyNotNullProperties(vo,entity);
            entity.setUpdateUser(currentUser.getId());
            entity.setUpdateUserName(user.getName());
            entity.setUpdateTime(new Date());
            if(entity.getPlanArea() != null && entity.getCurrentArea() != null){
                entity.setRemArea(entity.getPlanArea()-entity.getCurrentArea());
            }
        }
        this.saveOrUpdate(entity);
        return new SuccessResponseData();
    }

    /**
     * 根据ID批量删除计划信息
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

    @Override
    public LandPlanInfoVo getDetailById(Long id) {
        return this.baseMapper.getDetailById(id);
    }

    /**
     * 导入低效用地信息
     * @param result
     * @return
     */
    @Override
    public String uploadExcel(List result,String planType) {
        String msg = "";
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        for (int i = 0; i < result.size(); i++) {
            LandPlanExcelParam param = (LandPlanExcelParam) result.get(i);
            LandPlanInfoEntity main = new LandPlanInfoEntity();
            //时间（精确到毫秒）
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            //3位随机数
            String randomNumeric = RandomStringUtils.randomNumeric(3);
            String planCode = "P"+localDate + randomNumeric;
            BeanUtils.copyProperties(param,main);
            main.setCreateUser(currentUser.getId());
            main.setCreateUserName(currentUser.getName());
            main.setCreateTime(new Date());
            main.setPlanCode(planCode);
            main.setPlanType(planType);
            this.saveOrUpdate(main);
        }
        return msg;
    }
}
