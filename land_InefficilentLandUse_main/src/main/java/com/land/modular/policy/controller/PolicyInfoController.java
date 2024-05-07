package com.land.modular.policy.controller;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.policy.entity.SysPolicyInfoEntity;
import com.land.modular.policy.service.PolicyInfoService;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.warpper.UserWrapper;
import com.land.utils.FileCopyUtil;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
     * 根据类型分页
     * @param type
     * @param model
     * @return
     */
    @RequestMapping("/typePage")
    public String typePage(String type, Model model) {
        model.addAttribute("type",type);
        return PREFIX + "/typePage.html";
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
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("fileId") Long fileId,Model model) {
        SysPolicyInfoVo vo = policyInfoService.getDetailById(fileId);
        if(vo.getPolicyType().equals("1")){
            vo.setPolicyTypeName("国家级");
        }else if(vo.getPolicyType().equals("2")){
            vo.setPolicyTypeName("省级");
        }else if(vo.getPolicyType().equals("3")){
            vo.setPolicyTypeName("市级");
        }else if(vo.getPolicyType().equals("4")){
            vo.setPolicyTypeName("县级");
        }
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/detail.html";
    }

    /**
     * 查看文件
     * @param model
     * @return
     */
    @RequestMapping("/showPre")
    public  String showPre(Model model,@RequestParam(required = false)String fileId){
        SysPolicyInfoEntity entity = policyInfoService.selectById(fileId);
        String url = "";
        model.addAttribute("img",0);
        if(entity != null){
            File file = new File(entity.getFilePath());
            String projectPath = System.getProperty("user.dir");
            String targetPath = projectPath +"\\land_InefficilentLandUse_main\\target\\classes\\assets\\tmp";
            File folder = new File(targetPath);
            if(!folder.exists()){
                if(folder.mkdirs()){
                    System.out.println("文件夹创建成功！");
                }else{
                    System.out.println("文件夹创建失败！");
                }
            }
            if(entity.getFileSuffix().toLowerCase(Locale.ROOT).contains("pdf")){
                try {
                    FileCopyUtil.copy(file,targetPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                url = "/assets/tmp/"+file.getName().replaceAll("[\\\\/]", "");
            }else if(entity.getFileSuffix().toLowerCase(Locale.ROOT).contains("doc")){
                String uid = String.valueOf(UUID.randomUUID());
                url = "/assets/tmp/" + uid + ".pdf";
                //实例化Document类的对象
                Document doc = new Document();
                //加载Word
                doc.loadFromFile(entity.getFilePath());
                //保存为PDF格式
                doc.saveToFile(targetPath+"\\" + uid + ".pdf", FileFormat.PDF);
            }else{
                try {
                    FileCopyUtil.copy(file,targetPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                url = "/assets/tmp/"+file.getName().replaceAll("[\\\\/]", "");
                model.addAttribute("img",1);
            }
            model.addAttribute("path",url);
            model.addAttribute("entity",entity);
        }
        return PREFIX + "/previewPDF.html";
    }

    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit (@RequestParam("fileId") Long fileId,Model model) {
        SysPolicyInfoVo vo = policyInfoService.getDetailById(fileId);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/edit.html";
    }

    /**
     * 分页查询列表
     * @param timeLimit
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(@RequestParam(required = false) String policyType,@RequestParam(required = false) String policyName,
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
        vo.setPolicyType(policyType);
        vo.setPolicyName(policyName);
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
