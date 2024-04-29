package com.land.modular.landinfo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import cn.stylefeng.roses.kernel.model.response.ErrorResponseData;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.entity.LandDetailInfoHis;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.mapper.LandDetailDao;
import com.land.modular.landinfo.service.LandDetailHisService;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailExcelParam;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import com.land.modular.weekwork.entity.WeekWorkMain;
import com.land.modular.weekwork.vo.WeekWorkDetailExcelParam;
import com.land.sys.modular.system.entity.Dept;
import com.land.sys.modular.system.entity.FileInfo;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.service.DeptService;
import com.land.sys.modular.system.service.FileInfoService;
import com.land.sys.modular.system.service.UserService;
import com.land.utils.PinyinUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("landDetailService")
public class LandDetailServiceImpl  extends ServiceImpl<LandDetailDao, LandDetailInfo> implements LandDetailService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private LandDetailHisService landDetailHisService;
    /**
     * 根据条件查询列表数据
     * @param vo
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Map<String, Object>> selectList(LandDetailInfoVo vo, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        //vo.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, vo, beginTime, endTime);
    }

    /**
     * 批量导入数据
     * @param result
     * @return
     */
    @Override
    public String uploadExcel(List result) {
        String msg = "";
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        for (int i = 0; i < result.size(); i++) {
            LandDetailExcelParam param = (LandDetailExcelParam) result.get(i);
            LandDetailInfo main = new LandDetailInfo();
            BeanUtils.copyProperties(param,main);
            main.setCreateUser(currentUser.getId());
            main.setCreateUserName(currentUser.getName());
            main.setCreateTime(new Date());
            this.saveOrUpdate(main);
        }
        return msg;
    }

    /**
     * 数据导出
     * @param response
     * @param main
     * @return
     */
    @Override
    public String exportToExcel(HttpServletResponse response, LandInfo main,String beginTime, String endTime) {
        List<LandDetailInfo> list = this.baseMapper.selectByList(main,beginTime, endTime);
        //region 模板
        String exportExcelTemplate = (String) ConstantsContext.getExportExcelTemplate();
        File resource = null; //这种方法在linux下无法工作
        try {
            resource = ResourceUtils.getFile(exportExcelTemplate + "excel_landdetail.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TemplateExportParams params = new TemplateExportParams(resource.getPath());
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list != null && list.size() > 0) {
            /*for(LandInfo detail : list){

            }*/
        }else {
            LandDetailInfo resp = null;
            try {
                resp = LandDetailInfo.class.newInstance();
                list.add(resp);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        map.put("list", list);
        if (!StringUtils.isEmpty(main.getDeptName())) {
            map.put("deptName", main.getDeptName());
        }

        // map.put("createDate", sf.format(main.getCreateTime()));
        //map.put("mainRemark", main.getRemark());

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        // 判断数据
        if (workbook == null) {
            return "导出失败";
        }
        // 设置excel的文件名称
        // 重置响应对象
        response.reset();
        String fileName = null;
        try {
            fileName = URLEncoder.encode("低效用地详细信息表","UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename="+ fileName + ".xls");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 写出数据输出流到页面
        try {
            OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
            bufferedOutPut.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //endregion

        return "导出成功";
    }

    /**
     * 保存低效用地信息
     * @param landDetail
     */
    @Override
    public void saveLandDetail(LandDetailInfo landDetail) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        User user = userService.getById(currentUser.getId());
        if(StringUtils.isEmpty(landDetail.getId())){
            int nowYear = DateUtil.year(new Date());
            String landCode = "";
            //根据区县跟年份获取当前最大编号周期
            LandDetailInfo info = this.baseMapper.getByYearAndQx(nowYear,landDetail.getXdm());
            if(info != null){
                String numStr = info.getLandCode().substring(info.getLandCode().length() - 3);
                int code = Integer.valueOf(numStr) + 1;
                landCode = PinyinUtil.getPinYinHeadChar(landDetail.getXmc()) + nowYear + String.format("%03d",code);
                landDetail.setLandCode(landCode);
            }else if(!StringUtils.isEmpty(landDetail.getXmc())){
                landCode = PinyinUtil.getPinYinHeadChar(landDetail.getXmc()) + nowYear + "001";
                landDetail.setLandCode(landCode);
            }else{
                landCode = "SJZ" + nowYear + "001";
                landDetail.setLandCode(landCode);
            }
            landDetail.setYear(nowYear);
            landDetail.setCreateUser(currentUser.getId());
            landDetail.setCreateUserName(user.getName());
            landDetail.setCreateTime(new Date());
        }else{
            landDetail.setUpdateUser(currentUser.getId());
            landDetail.setUpdateUserName(user.getName());
            landDetail.setUpdateTime(new Date());
        }
        this.saveOrUpdate(landDetail);
    }

    /**
     * 批量删除数据
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
     * 获取详情
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LandDetailInfoVo getDetailById(Long id) {
        LandDetailInfoVo vo = this.baseMapper.getDetailById(id);
        if(vo.getBusinessKey() != null){
            List<FileInfo> fileInfoList = fileInfoService.getListByBusinessKey(vo.getBusinessKey());
            vo.setFileInfoList(fileInfoList);
        }
        return vo;
    }

    /**
     * 保存处置信息
     * @param landDetail
     * @param disType
     * @return
     */
    @Transactional
    @Override
    public ResponseData saveLandDis(LandDetailInfo landDetail, String disType) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        User user = userService.getById(currentUser.getId());
        if(!StringUtils.isEmpty(landDetail.getId())){
            LandDetailInfo detailInfo = this.baseMapper.selectById(landDetail.getId());
            LandDetailInfoHis his = new LandDetailInfoHis();
            BeanUtils.copyProperties(detailInfo,his);
            his.setOldId(detailInfo.getId());
            his.setId(null);
            his.setOpStatus("操作前");
            his.setOpUser(user.getUserId());
            his.setOpUserName(user.getName());
            his.setOpTime(new Date());
            //操作前
            landDetailHisService.saveHis(his);
            if("storage".equals(disType)){
                detailInfo.setLandStatus(disType);
                detailInfo.setZkfsx(landDetail.getZkfsx());
                detailInfo.setScStatus(landDetail.getScStatus());
                detailInfo.setCbhs(landDetail.getCbhs());
                detailInfo.setXyqd(landDetail.getXyqd());
                detailInfo.setTdgy(landDetail.getTdgy());
                detailInfo.setSczkfjz(landDetail.getSczkfjz());
            }
            detailInfo.setUpdateUser(currentUser.getId());
            detailInfo.setUpdateUserName(user.getName());
            detailInfo.setUpdateTime(new Date());
            this.saveOrUpdate(detailInfo);
            LandDetailInfoHis hisAfter = new LandDetailInfoHis();
            BeanUtils.copyProperties(detailInfo,hisAfter);
            hisAfter.setOldId(detailInfo.getId());
            hisAfter.setId(null);
            hisAfter.setOpStatus("操作后");
            hisAfter.setOpUser(user.getUserId());
            hisAfter.setOpUserName(user.getName());
            hisAfter.setOpTime(new Date());
            //操作后
            landDetailHisService.saveHis(his);
        }else {
            return new ErrorResponseData("未查询到要处置土地项");
        }

        return new SuccessResponseData();
    }
}
