package com.land.modular.landinfo.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.consts.ConstantsContext;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.vo.FileShareInfoVo;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailExcelParam;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.warpper.UserWrapper;
import com.land.utils.JsonFileUtil;
import com.land.utils.ZipUtil;
import org.beetl.ext.simulate.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 低效用地详细地块信息
 */
@Controller
@RequestMapping("landdetail")
public class LondDetailInfoController extends BaseController{
    private String PREFIX = "/landdetail";
    @Autowired
    private LandDetailService landDetailService;
    //所有低效用地详细地块信息首页
    @RequestMapping("/main")
    public String main(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/main.html";
    }
    //低效城镇
    @RequestMapping("/towns")
    public String towns(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/towns.html";
    }
    //低效产业管理
    @RequestMapping("/industries")
    public String industries(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/industries.html";
    }
    //低效村庄
    @RequestMapping("/villages")
    public String villages(String category, Model model) {
        model.addAttribute("category",category);
        return PREFIX + "/villages.html";
    }
    //低效规划TB
    @RequestMapping("/lowuseMap")
    public String lowuseMap(String category, Model model) {
        model.addAttribute("category",category);
        return  PREFIX + "/map.html";
    }


    @RequestMapping("/showOnMap")
    public String showOnMap(String key, String path,String value,String xmmc,Model model,HttpServletRequest request) {
        model.addAttribute("key",key);
        model.addAttribute("path",path);
        model.addAttribute("value",value);
        model.addAttribute("xmmc",xmmc);
        File file = new File("E:\\desktop\\低效用地数据\\dxghtb.zip");
        JSONObject jsonObject = ZipUtil.shpToGeoJsonByKey(file,key,value);
        JsonFileUtil.crateJson(jsonObject,value);
        //return  PREFIX + "/map.html";
        return  PREFIX + "/showMap.html";
    }

    @RequestMapping("/demo1")
    public String demo1(String category, Model model) {
        model.addAttribute("category",category);
        return  "/demos/map.html";
    }

    /**
     * 跳转新增页面
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add( Model model) {
        String businessKey = UUID.randomUUID().toString();
        model.addAttribute("businessKey",businessKey);
        return PREFIX + "/landDetailAdd.html";
    }
    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit (@RequestParam("id") Long id,Model model) {
        LandDetailInfoVo vo = landDetailService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/detailEdit.html";
    }
    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        LandDetailInfoVo vo = landDetailService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath",ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/landDetail.html";
    }

    /**
     * 保存
     * @param landDetail
     * @return
     */
    @BussinessLog(value = "保存低效用地项目信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveLandDetail")
    @ResponseBody
    public ResponseData saveLandDetail(@Valid LandDetailInfo landDetail) {
        this.landDetailService.saveLandDetail(landDetail);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public  ResponseData delete(String ids){
        boolean isDel = landDetailService.delete(ids);
        /*if(!isDel){
            return ResponseData.error("只能删除当前周的数据！");
        }*/
        return SUCCESS_TIP;
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

    /**
     * 数据批量导入
     * @param multipartFile
     * @param request
     * @return
     */
    @RequestMapping("/uploadExcel")
    @ResponseBody
    public ResponseData uploadExcel(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {

        String name = multipartFile.getOriginalFilename();
        request.getSession().setAttribute("upFile", name);
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            multipartFile.transferTo(new File(fileSavePath + name));

            File file = new File(fileSavePath + name);
            try {
                ImportParams params = new ImportParams();
                List result = ExcelImportUtil.importExcel(file, LandDetailExcelParam.class, params);
                if (result==null || result.size()==0){
                    return ResponseData.error("上传数据不能为空");
                }
                /*if (result.size()>=1000){
                    return ResponseData.error("单次最多上传1000条");
                }*/
                String retMsg = landDetailService.uploadExcel(result);

                return ResponseData.success(0, "上传成功",retMsg);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseData.error(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 数据导出
     * @param response
     * @param createUserName
     * @param deptName
     * @param category
     * @return
     * @throws Exception
     */
    @PostMapping("/exportToExcel")
    @ResponseBody
    public String exportToExcel(HttpServletResponse response, @RequestParam(required = false) String createUserName, @RequestParam(required = false) String deptName,

                                @RequestParam(required = false) String category,  @RequestParam(required = false) String timeLimit)throws Exception {

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
        return landDetailService.exportToExcel(response,main,beginTime, endTime);
    }

}
