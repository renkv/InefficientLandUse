package com.land.modular.weekwork.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import com.land.modular.weekwork.entity.WeekWorkMain;
import com.land.modular.weekwork.mapper.WeekWorkMainMapper;
import com.land.modular.weekwork.vo.WeekWorkDetailExcelParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mcrkw
 * @date 2021年04月19日 16:07
 */
@Service
public class WeekWorkMainService extends ServiceImpl<WeekWorkMainMapper, WeekWorkMain> {
    @Autowired
    private WeekWorkDetailService weekWorkDetailService;
    /**
     * @param main
    	 * @param beginTime
    	 * @param endTime
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @author mcrkw
     * @date 2021/4/20 14:53
     * 分页查询列表
     */
    public Page<Map<String, Object>> selectList(WeekWorkMain main, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        List<String> roleNames = currentUser.getRoleNames();
        if(roleNames.contains("管理员") || roleNames.contains("超级管理员")){
            return this.baseMapper.selectListByPage(page, main, beginTime, endTime,null,null);
        }else if(roleNames.contains("部长")){
            return this.baseMapper.selectListByPage(page, main, beginTime, endTime,null,currentUser.getDeptId());
        }
        return this.baseMapper.selectListByPage(page, main, beginTime, endTime,currentUser.getId(),null);
    }
    /**
     * @param weekWorkIngJson
    	 * @param weekWorkStopJson
    	 * @param weekWorkGJJson
    	 * @param weekWorkFinishJson
    	 * @param weekWorkJHGJJson
     * @return void
     * @author mcrkw
     * @date 2021/4/20 14:54
     * 保存
     */
    public void saveWeekWork(String remark,String weekWorkIngJson, String weekWorkStopJson, String weekWorkGJJson,
                             String weekWorkFinishJson, String weekWorkJHGJJson,String weekWorkPXJson,String weekWorkHYJson) {

        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        WeekWorkMain main = new WeekWorkMain();
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        Date now = new Date();
        main.setCreateUser(currentUser.getId());
        main.setCreateUserName(currentUser.getName());
        main.setDeptName(currentUser.getDeptName());
        main.setDeptId(currentUser.getDeptId());
        main.setCreateTime(now);
        main.setYear(year+"");
        main.setMonth(month+"");
        main.setRemark(remark);
        int week = 0;
        try {
            week = this.getWeekByWed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setWeek(week+"");
        this.saveOrUpdate(main);
        List <WeekWorkDetail> detailList = new ArrayList<>();
        if(!StringUtils.isEmpty(weekWorkIngJson)){
            List <WeekWorkDetail> inglist = JSONObject.parseArray(weekWorkIngJson, WeekWorkDetail.class);
            detailList.addAll(inglist);
        }
        if(!StringUtils.isEmpty(weekWorkStopJson)){
            List <WeekWorkDetail> stoplist = JSONObject.parseArray(weekWorkStopJson, WeekWorkDetail.class);
            detailList.addAll(stoplist);
        }
        if(!StringUtils.isEmpty(weekWorkGJJson)){
            List <WeekWorkDetail> gjlist = JSONObject.parseArray(weekWorkGJJson, WeekWorkDetail.class);
            detailList.addAll(gjlist);
        }
        if(!StringUtils.isEmpty(weekWorkFinishJson)){
            List <WeekWorkDetail> finishlist = JSONObject.parseArray(weekWorkFinishJson, WeekWorkDetail.class);
            detailList.addAll(finishlist);
        }
        if(!StringUtils.isEmpty(weekWorkJHGJJson)){
            List <WeekWorkDetail> jhgjlist = JSONObject.parseArray(weekWorkJHGJJson, WeekWorkDetail.class);
            detailList.addAll(jhgjlist);
        }
        if(!StringUtils.isEmpty(weekWorkPXJson)){
            List <WeekWorkDetail> pxlist = JSONObject.parseArray(weekWorkPXJson, WeekWorkDetail.class);
            detailList.addAll(pxlist);
        }
        if(!StringUtils.isEmpty(weekWorkHYJson)){
            List <WeekWorkDetail> hylist = JSONObject.parseArray(weekWorkHYJson, WeekWorkDetail.class);
            detailList.addAll(hylist);
        }
        detailList.forEach(item->{
            item.setCreateTime(now);
            item.setCreateUser(main.getCreateUser());
            item.setCreateUserName(main.getCreateUserName());
            item.setMainId(main.getId());
            weekWorkDetailService.saveOrUpdate(item);
        });

    }

    public int getWeekByWed() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值，变为周一
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        calendar.add(Calendar.DATE, 2);// 加两天变为周三
        //得到传入日期所在周的周三
        String wed = sdf.format(calendar.getTime());
        // 本月份的天数
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int wek = 3;//判断是周三
        int index = 0;//本月的第几个周三
        List list = new ArrayList();

        for (int i = 1; i <= days; i++) {

            calendar.set(Calendar.DAY_OF_MONTH, i);

            int weekd = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 注意,Calendar对象默认星期天为1

            if (weekd == wek) {

                String aaa = sdf.format(calendar.getTime());

                list.add(aaa);

                if (wed.equals(aaa)) {

                    index = list.size();

                }

            }

        }

        return index;
    }

    public String exportToExcel(HttpServletResponse response, Long id)  throws Exception {
        WeekWorkMain main = this.getById(id);
        List<WeekWorkDetail> detailList = weekWorkDetailService.getDetailListById(id);

        //region 模板
        String exportExcelTemplate = (String) ConstantsContext.getExportExcelTemplate();
        File resource = ResourceUtils.getFile(exportExcelTemplate + "excel_weekWork.xls"); //这种方法在linux下无法工作
        TemplateExportParams params = new TemplateExportParams(resource.getPath());

//            TemplateExportParams params = new TemplateExportParams("doc/excel_chubeiqichu.xls");
        Map<String, Object> map = new HashMap<String, Object>();
        List<WeekWorkDetail> mapIng = new ArrayList<>();
        List<WeekWorkDetail> mapStop = new ArrayList<>();
        List<WeekWorkDetail> mapGJ = new ArrayList<>();
        List<WeekWorkDetail> mapFinish = new ArrayList<>();
        List<WeekWorkDetail> mapJH = new ArrayList<>();
        List<WeekWorkDetail> mapPX = new ArrayList<>();
        List<WeekWorkDetail> mapHY = new ArrayList<>();
        if (detailList != null && detailList.size() > 0) {
            for(WeekWorkDetail detail : detailList){
                if(detail.getStatus().equals("0")){
                    mapIng.add(detail);
                    detail.setStatus("进行中");
                }else if(detail.getStatus().equals("1")){
                    mapStop.add(detail);
                    detail.setStatus("已暂停");
                }else if(detail.getStatus().equals("2")){
                    mapGJ.add(detail);
                    detail.setStatus("跟进中");
                }else if(detail.getStatus().equals("3")){
                    mapFinish.add(detail);
                    detail.setStatus("已完成");
                }else if(detail.getStatus().equals("4")){
                    mapJH.add(detail);
                    detail.setStatus("计划跟进中");
                }else if(detail.getStatus().equals("5")){
                    mapPX.add(detail);
                    detail.setStatus("培训");
                }else if(detail.getStatus().equals("6")){
                    mapHY.add(detail);
                    detail.setStatus("会议");
                }
            }
        }else {
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            detailList.add(resp);
        }
        if(mapIng.size()==0){
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            mapIng.add(resp);
        }
        if(mapStop.size()==0){
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            mapStop.add(resp);
        }
        if(mapGJ.size()==0){
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            mapGJ.add(resp);
        }
        if(mapFinish.size()==0){
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            mapFinish.add(resp);
        }
        if(mapJH.size()==0){
            WeekWorkDetail resp = WeekWorkDetail.class.newInstance();
            mapJH.add(resp);
        }
        map.put("mapIng", mapIng);
        map.put("mapStop", mapStop);
        map.put("mapGJ", mapGJ);
        map.put("mapFinish", mapFinish);
        map.put("mapJH", mapJH);
        map.put("detailList", detailList);
        if (!StringUtils.isEmpty(main.getDeptName())) {
            map.put("deptName", main.getDeptName());
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        map.put("createDate", sf.format(main.getCreateTime()));
        map.put("mainRemark", main.getRemark());

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        // 判断数据
        if (workbook == null) {
            return "导出失败";
        }
        // 设置excel的文件名称
        // 重置响应对象
        response.reset();
        String fileName = URLEncoder.encode("部门任务进度安排表"+main.getMonth()+"月"+main.getWeek()+"周("+main.getDeptName()+")","UTF-8");
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
     * @param result
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/4/28 16:36
     */
    @Transactional(rollbackFor = Exception.class)
    public String uploadExcel(List<WeekWorkDetailExcelParam> result) {
        String msg = "";
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        WeekWorkMain main = new WeekWorkMain();
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        Date now = new Date();
        main.setCreateUser(currentUser.getId());
        main.setCreateUserName(currentUser.getName());
        main.setDeptName(currentUser.getDeptName());
        main.setDeptId(currentUser.getDeptId());
        main.setCreateTime(now);
        main.setYear(year+"");
        main.setMonth(month+"");
        int week = 0;
        try {
            week = this.getWeekByWed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setWeek(week+"");
        this.saveOrUpdate(main);
        List <WeekWorkDetail> detailList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            int hangshu = i + 4;
            WeekWorkDetailExcelParam param = result.get(i);
            WeekWorkDetail detail = new WeekWorkDetail();
            detail.setSerialNo(param.getSerialNo());
            detail.setActualProgress(param.getActualProgress());
            detail.setAssignTime(param.getAssignTime());
           if("进行中项目".equals(param.getStatus())){
               detail.setStatus("0");
           }else if("暂停中项目".equals(param.getStatus())){
               detail.setStatus("1");
           }else if("跟进中项目".equals(param.getStatus())){
               detail.setStatus("2");
           }else if("已完成项目".equals(param.getStatus())){
               detail.setStatus("3");
           }else if("计划跟进中项目".equals(param.getStatus())){
               detail.setStatus("4");
           }else if("培训".equals(param.getStatus())){
               detail.setStatus("5");
           }else if("会议".equals(param.getStatus())){
               detail.setStatus("6");
           }
           detail.setImplementUser(param.getImplementUser());
           detail.setLeader(param.getLeader());
           detail.setPlanFinishTime(param.getPlanFinishTime());
           detail.setRemark(param.getRemark());
           detail.setSuperviseTime(param.getSuperviseTime());
           detail.setSupervision(param.getSupervision());
           detail.setTaskName(param.getTaskName());
           detailList.add(detail);
        }

        detailList.forEach(item->{
            item.setCreateTime(now);
            item.setCreateUser(main.getCreateUser());
            item.setCreateUserName(main.getCreateUserName());
            item.setMainId(main.getId());
            weekWorkDetailService.saveOrUpdate(item);
        });
        return msg;
    }
    /**
     * @param
     * @return boolean
     * @author mcrkw
     * @date 2021/4/29 10:31
     * 检查是否可以添加
     */
    public boolean checkAdd() {
        boolean result = false;
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        WeekWorkMain main = new WeekWorkMain();
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        main.setCreateUser(currentUser.getId());
        main.setCreateUserName(currentUser.getName());
        main.setDeptName(currentUser.getDeptName());
        main.setDeptId(currentUser.getDeptId());
        main.setYear(year+"");
        main.setMonth(month+"");
        int week = 0;
        try {
            week = this.getWeekByWed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setWeek(week+"");
        Integer count = this.baseMapper.getCount(main);
        if(count > 0){
            result = true;
        }
        return result;
    }

    public boolean checkDel(String ids) {
        boolean isDel = true;
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        int week = 0;
        try {
            week = this.getWeekByWed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] list = ids.split(",");
        for(String s : list){
            WeekWorkMain main = this.baseMapper.selectById(s);
            if(!main.getYear().equals(year+"") || !main.getMonth().equals(month+"") || !main.getWeek().equals(week+"")){
                isDel = false;
                break;
            }else{
                weekWorkDetailService.deleteByMainId(main.getId());
                this.baseMapper.deleteById(s);
            }
        }
        return isDel;
    }
}
