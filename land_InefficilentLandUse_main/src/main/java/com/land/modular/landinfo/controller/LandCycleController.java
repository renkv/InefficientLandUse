package com.land.modular.landinfo.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.pojo.page.LayuiPageFactory;
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
 * 低效用地全周期管理
 */
@Controller
@RequestMapping("/cycle")
public class LandCycleController {
    @Autowired
    private LandDetailService landDetailService;
    private String PREFIX = "/cycle";
    @RequestMapping("/main")
    public String main(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/main.html";
    }
    /**
     * 收储再开发 storage and redev
     */
    @RequestMapping("/stoAndRedev")
    public  String stoAndRedev(Model model){
        model.addAttribute("status","storage");
        return PREFIX + "/storage.html";
    }
    @RequestMapping("/selectListByStatus")
    @ResponseBody
    public Object selectListByStatus(@RequestParam(required = false) String createUserName,
                             @RequestParam(required = false) String status,@RequestParam(required = false) String xdm,@RequestParam(required = false) String xmmc,
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
        vo.setXmmc(xmmc);
        vo.setXdm(xdm);
        vo.setLandStatus("");
        vo.setLandStatus(status);
        vo.setCreateUserName(createUserName);
        Page<Map<String, Object>> list = landDetailService.selectList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }
}
