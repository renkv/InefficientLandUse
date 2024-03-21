package com.land.modular.landinfo.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.entity.FileShareInfo;
import com.land.modular.file.vo.FileShareInfoVo;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.service.LandInfoService;
import com.land.modular.weekwork.entity.WeekWorkMain;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.model.UploadResult;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 地块信息(LandInfo)表控制层
 *
 * @author makejava
 * @since 2021-06-23 16:11:44
 */
@Controller
@RequestMapping("landInfo")
public class LandInfoController extends BaseController {
    private String PREFIX = "/landinfo";
    /**
     * 服务对象
     */
    @Autowired
    private LandInfoService landInfoService;
    /**
     * @param category
    	 * @param model
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/24 10:23
     * 跳转新增页面
     */
    @RequestMapping("/add")
    public String add(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/landInfoAdd.html";
    }
    /**
     * @param
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/23 18:28
     * 三调
     */
    @RequestMapping("/three")
    public String three(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/infoMain.html";
    }

    /**
     * @param
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/23 18:28
     * 禁止类淘汰类
     */
    @RequestMapping("/stop")
    public String stop(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/infoMain.html";
    }
    /**
     * @param
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/23 18:35
     * 退二进三
     */
    @RequestMapping("/back")
    public String back(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/infoMain.html";
    }
    /**
     * @param
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/23 18:36
     * 确定规划
     */
    @RequestMapping("/sure")
    public String sure(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/infoMain.html";
    }
    /**
     * @param
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/6/23 18:36
     * 评价认定
     */
    @RequestMapping("/confirm")
    public String confirm(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/infoMain.html";
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public LandInfo selectOne(Long id) {
        return this.landInfoService.queryById(id);
    }
    /**
     * @param createUserName
         * @param deptName
         * @param timeLimit
     * @return java.lang.Object
     * @author mcrkw
     * @date 2021/6/24 10:54
     * 查询列表
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
        LandInfo main = new LandInfo();
        main.setCategory(category);
        main.setCreateUserName(createUserName);
        main.setDeptName(deptName);
        Page<Map<String, Object>> list = landInfoService.selectList(main, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        LandInfo info = landInfoService.queryById(id);
        model.addAttribute("info",info);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/infoDetail.html";
    }
    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit (@RequestParam("id") Long id,Model model) {
        LandInfo info = landInfoService.queryById(id);
        model.addAttribute("info",info);
        model.addAttribute("ctxPath",ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/infoEdit.html";
    }

    /**
     * 保存
     * @param landInfo
     * @return
     */
    @BussinessLog(value = "保存低效用地项目信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveLandInfo")
    @ResponseBody
    public ResponseData saveLandInfo(@Valid LandInfo landInfo) {
        this.landInfoService.saveLandInfo(landInfo);
        return SUCCESS_TIP;
    }
    /**
     * @param ids
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     * @author mcrkw
     * @date 2021/6/24 11:16
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public  ResponseData delete(String ids){
        boolean isDel = landInfoService.delete(ids);
        if(!isDel){
            return ResponseData.error("只能删除当前周的数据！");
        }
        return SUCCESS_TIP;
    }
    /**
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/7/1 15:19
     */
    @PostMapping("/exportToExcel")
    @ResponseBody
    public String exportToExcel(HttpServletResponse response, @RequestParam(required = false) String createUserName, @RequestParam(required = false) String deptName,
                                @RequestParam(required = false) String category)throws Exception {
        LandInfo main = new LandInfo();
        main.setCategory(category);
        main.setCreateUserName(createUserName);
        main.setDeptName(deptName);
        return landInfoService.exportToExcel(response,main);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/areaUpload")
    @ResponseBody
    public ResponseData areaUpload(@RequestPart("file") MultipartFile file) {
        ResponseData data = this.landInfoService.areaUpload(file);
        return data;
    }

}
