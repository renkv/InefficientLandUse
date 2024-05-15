package com.land.modular.plan.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.plan.service.LandPlanInfoService;
import com.land.modular.plan.vo.LandPlanInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * 实施计划方法类
 */
@Controller
@RequestMapping("/plan")
public class LandPlanController extends BaseController {
    private String PREFIX = "/plan";
    @Autowired
    private LandPlanInfoService landPlanInfoService;
    /**
     * 总实施计划
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main( Model model) {
        return PREFIX + "/main.html";
    }

    /**
     * 根据类型跳转页面
     */
    @RequestMapping("/gotoPageByType")
    public String gotoPageByType(String planType, Model model) {
        if(planType.equals("1")){
            //低效企业
            return PREFIX + "/businesses.html";
        }else if(planType.equals("2")){
            //城中村改造
            return PREFIX + "/village.html";
        }else if(planType.equals("3")){
            //城市更新
            return PREFIX + "/reCity.html";
        }else if(planType.equals("4")){
            //开发区低效处置
            return PREFIX + "/zone.html";
        }else if(planType.equals("5")){
            //其他处置
            return PREFIX + "/other.html";
        }
        return PREFIX + "/main.html";
    }

    /**
     * 跳转新增页面
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(@RequestParam String planType, Model model) {
        /*String businessKey = UUID.randomUUID().toString();
        model.addAttribute("businessKey",businessKey);*/
        model.addAttribute("planType",planType);
        if(planType.equals("1")){
            //低效企业
            return PREFIX + "/busAdd.html";
        }else if(planType.equals("2")){
            //城中村改造
            return PREFIX + "/villagesAdd.html";
        }else if(planType.equals("3")){
            //城市更新
            return PREFIX + "/reCityAdd.html";
        }else if(planType.equals("4")){
            //开发区低效处置
            return PREFIX + "/zoneAdd.html";
        }else if(planType.equals("5")){
            //其他处置
            return PREFIX + "/otherAdd.html";
        }
        return PREFIX + "/landDetailAdd.html";
    }

    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit (@RequestParam("id") Long id,Model model) {
        LandPlanInfoVo vo = landPlanInfoService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        if(vo.getPlanType().equals("1")){
            return PREFIX + "/busEdit.html";
        }else if(vo.getPlanType().equals("2")){
            //城中村改造
            return PREFIX + "/villagesEdit.html";
        }else if(vo.getPlanType().equals("3")){
            //城市更新
            return PREFIX + "/reCityEdit.html";
        }else if(vo.getPlanType().equals("4")){
            //开发区低效处置
            return PREFIX + "/zoneEdit.html";
        }else if(vo.getPlanType().equals("5")){
            //其他处置
            return PREFIX + "/otherEdit.html";
        }
        return PREFIX + "/otherEdit.html";
    }

    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        LandPlanInfoVo vo = landPlanInfoService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath",ConfigListener.getConf().get("contextPath"));
        if(vo.getPlanType().equals("1")){
            return PREFIX + "/busDetail.html";
        }else if(vo.getPlanType().equals("villages"))
        {
            return PREFIX + "/villagesDetaiL.html";
        }else if(vo.getPlanType().equals("3")){
            //城市更新
            return PREFIX + "/reCityDetail.html";
        }else if(vo.getPlanType().equals("4")){
            //开发区低效处置
            return PREFIX + "/zoneDetail.html";
        }else if(vo.getPlanType().equals("5")){
            //其他处置
            return PREFIX + "/otherDetail.html";
        }
        return PREFIX + "/otherDetail.html";
    }

    /**
     * 分页查询计划信息
     * @param createUserName
     * @param category
     * @param xdm
     * @param xmmc
     * @param timeLimit
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(@RequestParam(required = false) String planName,
                             @RequestParam(required = false) String planType,
                             @RequestParam(required = false) String busName,
                             @RequestParam(required = false) String villageName,
                             @RequestParam(required = false) String renewalName,
                             @RequestParam(required = false) String zoneName,
                             @RequestParam(required = false) String xmmc,
                             @RequestParam(required = false) String timeLimit) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        LandPlanInfoVo vo = new LandPlanInfoVo();
        vo.setPlanType(planType);
        vo.setPlanName(planName);
        vo.setBusName(busName);
        vo.setXmmc(xmmc);
        vo.setVillageName(villageName);
        vo.setZoneName(zoneName);
        vo.setRenewalName(renewalName);
        //main.setDeptName(deptName);
        Page<Map<String, Object>> list = landPlanInfoService.selectList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 保存实施计划信息
     * @return
     */
    @BussinessLog(value = "保存实施计划信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/savePlan")
    @ResponseBody
    public ResponseData savePlan(@Valid LandPlanInfoVo vo,  @RequestParam(required = true) String landCode) {
        vo.setLandCode(landCode);
        return landPlanInfoService.savePlan(vo);
    }

    /**
     * 根据id删除计划信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public  ResponseData delete(String ids){
        boolean isDel = landPlanInfoService.delete(ids);
        return SUCCESS_TIP;
    }
}
