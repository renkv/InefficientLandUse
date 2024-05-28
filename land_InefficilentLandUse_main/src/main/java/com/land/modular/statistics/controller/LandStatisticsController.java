package com.land.modular.statistics.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.plan.service.LandPlanInfoService;
import com.land.modular.statistics.vo.LandStaVo;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
