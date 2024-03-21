package com.land.modular.file.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.entity.FileShareInfo;
import com.land.modular.file.service.FileShareService;
import com.land.modular.file.vo.FileShareInfoVo;
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
 * 文件分享方法
 */
@Controller
@RequestMapping("/fileShare")
public class FileShareController extends BaseController {
    private String PREFIX = "/file/share";
    @Autowired
    private FileShareService fileShareService;
    /**
     * 跳转到主页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/shareMain.html";
    }

    /**
     * 新增页面
     */
    @RequestMapping("/add")
    public String add(Model model) {
        String businessKey = UUID.randomUUID().toString();
        model.addAttribute("businessKey",businessKey);
        return PREFIX + "/shareAdd.html";
    }

    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit (@RequestParam("shareId") Long shareId,Model model) {
        FileShareInfoVo shareInfo = fileShareService.getDetailById(shareId);
        model.addAttribute("shareInfo",shareInfo);
        model.addAttribute("ctxPath",ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/shareEdit.html";
    }
    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("shareId") Long shareId,Model model) {
        FileShareInfoVo shareInfo = fileShareService.getDetailById(shareId);
        model.addAttribute("shareInfo",shareInfo);
        model.addAttribute("ctxPath",ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/shareDetail.html";
    }

    /**
     *
     * @param timeLimit
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String shareTitle,@RequestParam(required = false) String shareNo,@RequestParam(required = false) String shareType,
                       @RequestParam(required = false) String timeLimit) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        FileShareInfo info = new FileShareInfo();
        info.setShareNo(shareNo);
        info.setShareType(shareType);
        info.setShareTitle(shareTitle);
        Page<Map<String, Object>> users = fileShareService.selectShareList(info, beginTime, endTime);
        Page wrapped = new UserWrapper(users).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 保存发送
     * @param fileShareInfo
     * @return
     */
    @BussinessLog(value = "保存发送文件信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveFileShare")
    @ResponseBody
    public ResponseData saveFileShare(@Valid FileShareInfo fileShareInfo) {
        this.fileShareService.saveFileShare(fileShareInfo);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     * @param shareId
     * @return
     */
    @BussinessLog(value = "删除", key = "shareId", dict = DeptDict.class)
    @RequestMapping(value = "/deleteById")
    @ResponseBody
    public ResponseData deleteById(@RequestParam("shareId") Long shareId) {
        this.fileShareService.deleteById(shareId);
        return SUCCESS_TIP;
    }
}
