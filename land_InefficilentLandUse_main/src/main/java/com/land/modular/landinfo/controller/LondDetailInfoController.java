package com.land.modular.landinfo.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 低效用地详细地块信息
 */
@Controller
@RequestMapping("landdetail")
public class LondDetailInfoController {
    private String PREFIX = "/landdetail";
    @Autowired
    private LandDetailService landDetailService;
    //低效用地详细地块信息首页
    @RequestMapping("/main")
    public String main(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/main.html";
    }

    /**
     * 查询列表
     * @param createUserName
     * @param deptName
     * @param category
     * @param timeLimit
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(@RequestParam(required = false) String createUserName, @RequestParam(required = false) String deptName,
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) String timeLimit) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        LandDetailInfoVo vo = new LandDetailInfoVo();
       // main.setCategory(category);
        vo.setCreateUserName(createUserName);
        //main.setDeptName(deptName);
        Page<Map<String, Object>> list = landDetailService.selectList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }
}
