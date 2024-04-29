package com.land.modular.landinfo.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

/**
 * 低效用地全周期管理
 */
@Controller
@RequestMapping("/cycle")
public class LandCycleController extends BaseController {
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

    /**
     * 新增收储再开发项目
     * @param model
     * @return
     */
    @RequestMapping("/addStorage")
    public  String addStorage(Model model){
        model.addAttribute("status","storage");
        return PREFIX + "/addStorage.html";
    }
    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        LandDetailInfoVo vo = landDetailService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        if(vo.getLandStatus().equals("storage")){
            if(!StringUtils.isEmpty(vo.getScStatus())){
                if("1".equals(vo.getScStatus())){
                    vo.setScStatus("拟收储");
                }else if("2".equals(vo.getScStatus())){
                    vo.setScStatus("已收储");
                }else if("2".equals(vo.getScStatus())){
                    vo.setScStatus("待开发");
                }else if("4".equals(vo.getScStatus())){
                    vo.setScStatus("已开发");
                }
            }
            return PREFIX + "/storageDetail.html";
        }
        return PREFIX + "/landDetail.html";
    }

    /**
     * 根据状态查询
     * @param createUserName
     * @param status
     * @param xdm
     * @param xmmc
     * @param timeLimit
     * @return
     */
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
    @BussinessLog(value = "保存低效用地项目处置信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveLandDis")
    @ResponseBody
    public ResponseData saveLandDis(@Valid LandDetailInfo landDetail,@RequestParam(required = false) String disType,@RequestParam(required = true) Long id) {
        landDetail.setId(id);
        return landDetailService.saveLandDis(landDetail,disType);
    }
}
