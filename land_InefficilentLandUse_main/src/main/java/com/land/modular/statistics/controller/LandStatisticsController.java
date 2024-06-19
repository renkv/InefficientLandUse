package com.land.modular.statistics.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.plan.service.LandPlanInfoService;
import com.land.modular.statistics.vo.InBusinessVo;
import com.land.modular.statistics.vo.LandStaVo;
import com.land.modular.statistics.vo.XmSelectVo;
import com.land.sys.modular.system.entity.Dict;
import com.land.sys.modular.system.service.DictService;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 统计
 */
@Controller
@RequestMapping("statistics")
public class LandStatisticsController {
    private String PREFIX = "/statistics";
    @Autowired
    private LandDetailService landDetailService;
    @Autowired
    private LandPlanInfoService landPlanInfoService;
    @Autowired
    private DictService dictService;

    /**
     * 低效用地基础数据统计
     * @param model
     * @return
     */
    @RequestMapping("/landSta")
    public String landSta(Model model) {
        return PREFIX + "/landSta.html";
    }

    /**
     * 全周期进展统计页面
     * @param model
     * @return
     */
    @RequestMapping("/cycleSta")
    public String cycleSta(Model model) {
        return PREFIX + "/cycleSta.html";
    }

    /**
     * 困难原因
     * @param model
     * @return
     */
    @RequestMapping("/diffSta")
    public String diffSta(Model model) {
        return PREFIX + "/diffSta.html";
    }
    /**
     * 低效企业进展统计
     */
    @RequestMapping("/inbusiness")
    public String inbusiness(Model model) {
        List<Dict> qxList = dictService.listDictsByCode("sjzqx");
        List<Map<String,Object>> yearList = landPlanInfoService.getDistinctYear();
        List<XmSelectVo> list = new ArrayList<>();
        if(yearList != null && yearList.size() > 0){
            for  (Map<String, Object> m : yearList){
                for  (String k : m.keySet()){
                    System.out.println(k +  " : "  + m.get(k));
                    XmSelectVo vo = new XmSelectVo();
                    vo.setName(m.get(k).toString());
                    vo.setValue(m.get(k).toString());
                    list.add(vo);
                }
            }
        }
        model.addAttribute("qxList",qxList);
        model.addAttribute("yearJson", JSON.toJSONString(list));
        return PREFIX + "/inbusiness.html";
    }

    /**
     * 低效企业查询
     * @param xdm
     * @param timeLimit
     * @return
     */
    @RequestMapping("/inbusList")
    @ResponseBody
    public Object inbusList(InBusinessVo vo,@RequestParam(required = false) String timeLimit) {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if(ToolUtil.isNotEmpty(vo.getYear())){
            List<String> yearList = new ArrayList<>();
            List<Map<String, String>> listObjectFir = (List<Map<String, String>>) JSONArray.parse(vo.getYear());
            // 利用JSONArray中的parse方法来解析json数组字符串
            for (Map<String, String> mapList : listObjectFir) {
                for (Map.Entry entry : mapList.entrySet()) {
                    if(entry.getKey().equals("value")){
                        yearList.add(entry.getValue().toString());
                    }
                    System.out.printf("年份", entry.getKey(), entry.getValue());
                }
            }
            vo.setYearList(yearList);
        }

        List<InBusinessVo> list = landPlanInfoService.inbusListNoPage(vo, beginTime, endTime);
        //亩取整
        if(list != null && list.size() > 0 && (vo.getFlag() == null || vo.getFlag() == 1)){
            DecimalFormat df = new DecimalFormat("#");
            DecimalFormat df2 = new DecimalFormat("#.00");
            for(InBusinessVo bus : list){
                String totalAreaStr = df.format(bus.getTotalArea());
                Double totalArea = Double.valueOf(totalAreaStr);
                bus.setTotalArea(totalArea);
                String finishAreaStr = df.format(bus.getFinishArea());
                Double finishArea = Double.valueOf(finishAreaStr);
                bus.setFinishArea(finishArea);
                BigDecimal value = BigDecimal.valueOf(totalArea);
                if(value.compareTo(BigDecimal.ZERO) != 0){
                    Double com = (finishArea/totalArea)*100;
                    String str = df2.format(com);
                    bus.setComratio(Double.valueOf(str));
                }
            }
        }
        //公顷保留两位小数
        if(list != null && list.size() > 0 && vo.getFlag() != null && vo.getFlag() == 2){
            DecimalFormat df = new DecimalFormat("#.00");
            for(InBusinessVo bus : list){
                String totalAreaStr = df.format(bus.getTotalArea()/15);
                Double totalArea = Double.valueOf(totalAreaStr);
                bus.setTotalArea(totalArea);
                String finishAreaStr = df.format(bus.getFinishArea()/15);
                Double finishArea = Double.valueOf(finishAreaStr);
                bus.setFinishArea(finishArea);
                BigDecimal value = BigDecimal.valueOf(totalArea);
                if(value.compareTo(BigDecimal.ZERO) != 0){
                    Double com = (finishArea/totalArea)*100;
                    String str = df.format(com);
                    bus.setComratio(Double.valueOf(str));
                }
            }
        }
        return new SuccessResponseData(0,"请求成功",list);
    }

    /**
     * 项目基础信息统计
     * @param vo
     * @param timeLimit
     * @return
     */
    @RequestMapping("/landStaList")
    @ResponseBody
    public Object landStaList(@RequestParam(required = false) String category,@RequestParam(required = false) String xdm,@RequestParam(required = false) String timeLimit) {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        LandStaVo vo = new LandStaVo();
        vo.setCategory(category);
        vo.setXdm(xdm);
        Page<Map<String, Object>> list = landDetailService.landStaList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 全周期统计查询
     * @param landStatus
     * @param xdm
     * @param timeLimit
     * @return
     */
    @RequestMapping("/cycleStaList")
    @ResponseBody
    public Object cycleStaList(@RequestParam(required = false) String landStatus,@RequestParam(required = false) String xdm,@RequestParam(required = false) String timeLimit) {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        LandStaVo vo = new LandStaVo();
        vo.setLandStatus(landStatus);
        vo.setXdm(xdm);
        Page<Map<String, Object>> list = landDetailService.cycleStaList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    @RequestMapping("/diffStaList")
    @ResponseBody
    public Object diffStaList(@RequestParam(required = false) String landStatus,@RequestParam(required = false) String xdm,@RequestParam(required = false) String timeLimit) {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        LandStaVo vo = new LandStaVo();
        vo.setXdm(xdm);
        Page<Map<String, Object>> list = landPlanInfoService.diffStaList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 低效企业数据导出
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/exportToBusExcel")
    @ResponseBody
    public String exportToBusExcel(HttpServletResponse response,InBusinessVo vo,@RequestParam(required = false) String timeLimit)throws Exception {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        if(ToolUtil.isNotEmpty(vo.getYear())){
            List<String> yearList = new ArrayList<>();
            List<Map<String, String>> listObjectFir = (List<Map<String, String>>) JSONArray.parse(vo.getYear());
            // 利用JSONArray中的parse方法来解析json数组字符串
            for (Map<String, String> mapList : listObjectFir) {
                for (Map.Entry entry : mapList.entrySet()) {
                    if(entry.getKey().equals("value")){
                        yearList.add(entry.getValue().toString());
                    }
                    System.out.printf("年份", entry.getKey(), entry.getValue());
                }
            }
            vo.setYearList(yearList);
        }
        return landPlanInfoService.exportToBusExcel(response,vo,beginTime, endTime);
    }

}
