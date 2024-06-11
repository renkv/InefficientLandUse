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
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailExcelParam;
import com.land.modular.plan.entity.LandPlanInfoEntity;
import com.land.modular.plan.mapper.LandPlanDao;
import com.land.modular.plan.service.LandPlanInfoService;
import com.land.modular.plan.vo.LandPlanExcelParam;
import com.land.modular.plan.vo.LandPlanInfoVo;

import com.land.modular.statistics.vo.LandStaVo;
import com.land.sys.modular.system.entity.Dict;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.service.DictService;
import com.land.sys.modular.system.service.UserService;
import com.land.utils.BeanCopyUtils;
import com.land.utils.GetAndSetField;
import com.land.utils.PinyinUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("landPlanInfoService")
public class LandPlanInfoServiceImpl extends ServiceImpl<LandPlanDao, LandPlanInfoEntity> implements LandPlanInfoService {
    @Autowired
    private UserService userService;
    @Autowired
    private LandDetailService landDetailService;
    @Autowired
    private DictService dictService;
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
        if(currentUser.getDeptName() != null && !(currentUser.getDeptName().equals("石家庄市自然资源与规划局"))){
            vo.setCountyName(currentUser.getDeptName());
        }
        //vo.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, vo, beginTime, endTime);
    }

    /**
     * 保存实施计划
     * @param vo
     * @return
     */
    @Override
    @Transactional
    public ResponseData savePlan(LandPlanInfoVo vo,LandDetailInfo landDetail) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        LandDetailInfo en;
        if(StringUtils.isEmpty(vo.getLandCode())){
            landDetail.setId(null);
             en = landDetailService.saveLandDetail(landDetail);
            vo.setLandCode(en.getLandCode());
        }else{
            en = landDetailService.selectByLandCode(vo.getLandCode());
        }
        if(en == null){
            return ResponseData.error("低效用地项目不存在！");
        }
        vo.setCountyName(en.getXmc());
        vo.setCountyCode(en.getXdm());
        User user = userService.getById(currentUser.getId());
        LandPlanInfoEntity entity = new LandPlanInfoEntity();
        if(StringUtils.isEmpty(vo.getId())){
            //判断企业是否已经存在
            List<LandPlanInfoEntity> existList = this.baseMapper.selectByName(vo.getBusName());
            if(existList != null && existList.size() > 0){
                return ResponseData.error("企业名称"+vo.getBusName()+"已经存在");
            }
            //时间（精确到毫秒）
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String localDate = LocalDateTime.now().format(ofPattern);
            //3位随机数
            String randomNumeric = RandomStringUtils.randomNumeric(3);
            String planCode = "P"+localDate + randomNumeric;
            BeanCopyUtils.copyNotNullProperties(vo,entity);
            if(!(StringUtils.isEmpty(entity.getPlanType())) && entity.getPlanType().equals("1")){
                entity.setPlanName(entity.getBusName());
            }
            int nowYear = DateUtil.year(new Date());
            entity.setYear(nowYear+"");
            entity.setPlanCode(planCode);
            entity.setCreateUser(currentUser.getId());
            entity.setCreateUserName(user.getName());
            entity.setCreateTime(new Date());
            if(entity.getPlanArea() != null && entity.getCurrentArea() != null){
                entity.setRemArea(entity.getOccupyArea()-entity.getCurrentArea());
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
        StringBuffer stringBuffer = new StringBuffer();
        List<LandPlanInfoEntity> inserList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            LandPlanExcelParam param = (LandPlanExcelParam) result.get(i);
            int coNum = i + 1;
            LandPlanInfoEntity main = new LandPlanInfoEntity();
            //判断企业是否已经存在
            List<LandPlanInfoEntity> existList = this.baseMapper.selectByName(param.getBusName());
            Dict deptDict = dictService.getOneByNameAndCode(param.getCountyName(),"sjzqx");
            if(deptDict != null){
                main.setCountyCode(deptDict.getCode());
            }else{
                stringBuffer.append("第" + coNum +"行，县名称:"+param.getCountyName()+"不存在！");
            }
            if(existList != null && existList.size() > 0){
                stringBuffer.append("第" + coNum +"行，"+param.getBusName()+"已存在！");
            }

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
            if(main.getPlanArea() != null && main.getCurrentArea() != null){
                main.setRemArea(main.getPlanArea()-main.getCurrentArea());
            }
            inserList.add(main);
        }
        if(!stringBuffer.toString().equals("")){
            return stringBuffer.toString();
        }else{
            this.saveOrUpdateBatch(inserList);
        }
        return msg;
    }

    /**
     * 统计
     * @param vo
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Map<String, Object>> diffStaList(LandStaVo vo, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.diffStaList(page, vo, beginTime, endTime);
    }

    /**
     * 根据ID跟字段保存数据
     * @param id
     * @param field
     * @param value
     * @return
     */
    @Override
    public ResponseData savePlanById(Long id, String field, String value) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        if(id != null){
            LandPlanInfoEntity entity = this.baseMapper.selectById(id);

            if(entity != null){
                Field[] fields = LandPlanInfoEntity.class.getDeclaredFields();
                int isContain = 0;
                for(Field f :fields){
                    if(f.getName().equals(field)){
                        isContain = 1;
                        switch (f.getType().getSimpleName()) {
                            case "int":
                            case "Integer":
                                GetAndSetField.setFieldValueByName(entity,f.getName(),Integer.valueOf(value));
                                break;
                            case "long":
                            case "Long":
                                GetAndSetField.setFieldValueByName(entity,f.getName(),Long.valueOf(value));
                                break;
                            case "String":
                                GetAndSetField.setFieldValueByName(entity,f.getName(),value);
                                break;
                            case "double":
                            case "Double":
                                GetAndSetField.setFieldValueByName(entity,f.getName(),Double.valueOf(value));
                                break;
                            default:
                                // 如果字段名不符合要求，返回错误信息
                                return ResponseData.error("提交的数据有问题，请检查！");
                        }
                        break;
                    }
                }
                if(isContain == 1){
                    entity.setUpdateUser(currentUser.getId());
                    entity.setUpdateUserName(currentUser.getName());
                    entity.setUpdateTime(new Date());
                    if(entity.getPlanArea() != null && entity.getCurrentArea() != null){
                        entity.setRemArea(entity.getPlanArea()-entity.getCurrentArea());
                    }
                    this.saveOrUpdate(entity);
                    return new SuccessResponseData();
                }else{
                    // 如果字段名不符合要求，返回错误信息
                    return ResponseData.error("提交的数据有问题，请检查！");
                }
            }
        }
        return ResponseData.error("请求数据不存在");
    }
}
