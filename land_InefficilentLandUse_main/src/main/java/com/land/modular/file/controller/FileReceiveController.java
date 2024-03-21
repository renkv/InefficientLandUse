package com.land.modular.file.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.entity.FileShareInfo;
import com.land.modular.file.service.FileReceiveService;
import com.land.modular.file.service.FileShareService;
import com.land.modular.file.vo.FileShareInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.model.UploadResult;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件接受方法
 */
@Controller
@RequestMapping("/fileReceive")
public class FileReceiveController extends BaseController {
    private String PREFIX = "/file/receive";
    @Autowired
    private FileReceiveService fileReceiveService;
    @Autowired
    private FileShareService fileShareService;

    /**
     * 跳转到主页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/receiveMain.html";
    }
    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        FileShareInfoVo shareInfo = fileReceiveService.getDetailById(id);
        model.addAttribute("shareInfo",shareInfo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/receiveDetail.html";
    }

    /**
     * 跳转转发页面
     */
    @RequestMapping("/toForward")
    public String toForward(@RequestParam("id") Long id,Model model) {
        FileShareInfoVo shareInfo = fileReceiveService.getDetailById(id);
        String businessKey = UUID.randomUUID().toString();
        model.addAttribute("businessKey",businessKey);
        model.addAttribute("shareInfo",shareInfo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/fileForward.html";
    }

    /**
     * 跳转转发页面
     */
    @RequestMapping("/toReply")
    public String toReply(@RequestParam("id") Long id,Model model) {
        model.addAttribute("id",id);
        return PREFIX + "/fileReply.html";
    }

    /**
     *
     * @param timeLimit
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String shareTitle,
                       @RequestParam(required = false) String timeLimit) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> users = fileReceiveService.selectReceiveList(shareTitle, beginTime, endTime);
        Page wrapped = new UserWrapper(users).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     * 保存转发
     * @param fileShareInfo
     * @return
     */
    @BussinessLog(value = "保存转发文件信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveFileForward")
    @ResponseBody
    public ResponseData saveFileForward(@Valid FileShareInfo fileShareInfo,@RequestParam("oldShareId") String oldShareId) {
        fileShareService.saveFileForward(fileShareInfo,oldShareId);
        return SUCCESS_TIP;
    }

    /**
     * 保存回复信息
     * @param replyInfo
     * @param id
     * @return
     */
    @RequestMapping(value = "/saveFileReply")
    @ResponseBody
    public ResponseData saveFileReply(@RequestParam("replyInfo") String replyInfo,@RequestParam("id") Long id) {
        fileReceiveService.saveFileReply(replyInfo,id);
        return SUCCESS_TIP;
    }
    /**
     *
     * @author mcrkw
     * @date 2021/4/16 10:37
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     */
    @RequestMapping(method = RequestMethod.POST, path = "/getNewFileNum")
    @ResponseBody
    public ResponseData getNewFileNum() {
        int num = fileReceiveService.getNewFileNum();
        HashMap<String, Object> tjMap = fileReceiveService.getFileStatistics();
        HashMap<String, Object> map = new HashMap<>();
        map.put("num", num);
        map.putAll(tjMap);
        return ResponseData.success(200, "获取成功", map);
    }

}
