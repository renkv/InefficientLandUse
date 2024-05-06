package com.land.modular.policy.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.policy.service.PolicyInfoService;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * 政策管理方法类
 */
@Controller
@RequestMapping("/policy")
public class PolicyInfoController extends BaseController {
    @Autowired
    private PolicyInfoService policyInfoService;
    private String PREFIX = "/policy";

    /**
     * 政策管理首页
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main( Model model) {
        return PREFIX + "/main.html";
    }

    /**
     * 跳转新增页面
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public  String add(Model model){
        return PREFIX + "/add.html";
    }

    /**
     * 分页查询列表
     * @param createUserName
     * @param status
     * @param xdm
     * @param xmmc
     * @param timeLimit
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(@RequestParam(required = false) String createUserName,
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
        SysPolicyInfoVo vo = new SysPolicyInfoVo();

        vo.setCreateUserName(createUserName);
        Page<Map<String, Object>> list = policyInfoService.selectList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 保存政策
     * @return
     */
    @BussinessLog(value = "保存政策信息", key = "id", dict = DeptDict.class)
    @RequestMapping(value = "/savePolicy")
    @ResponseBody
    public ResponseData savePolicy(@RequestPart("file") MultipartFile file,SysPolicyInfoVo vo) {
        return policyInfoService.savePolicy(file,vo);
    }

    /**
     * 删除政策信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public  ResponseData delete(String ids){
        boolean isDel = policyInfoService.delete(ids);
        return SUCCESS_TIP;
    }
}
