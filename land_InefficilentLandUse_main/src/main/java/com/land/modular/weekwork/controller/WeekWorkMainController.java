package com.land.modular.weekwork.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import com.land.modular.weekwork.entity.WeekWorkMain;
import com.land.modular.weekwork.service.WeekWorkDetailService;
import com.land.modular.weekwork.service.WeekWorkMainService;
import com.land.modular.weekwork.vo.WeekWorkDetailExcelParam;
import com.land.sys.core.listener.ConfigListener;
import com.land.sys.modular.system.entity.Dept;
import com.land.sys.modular.system.service.DeptService;
import com.land.sys.modular.system.warpper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mcrkw
 * @date 2021年04月19日 15:06
 */
@Controller
@RequestMapping("/weekWork")
public class WeekWorkMainController extends BaseController {

    @Autowired
    private WeekWorkDetailService weekWorkDetailService;

    private String PREFIX = "/weekwork/main";
    @Autowired
    private WeekWorkMainService weekWorkMainService;
    @Autowired
    private DeptService deptService;
    /**
     * @param 
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/4/19 15:51
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/weekWorkMain.html";
    }
    /**
     * @param model
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/4/20 8:31
     * 跳转新增页面
     */
    @RequestMapping("/add")
    public String add(Model model) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        Dept dept = deptService.getById(currentUser.getDeptId());
        model.addAttribute("deptName",dept.getFullName());
        return PREFIX + "/weekWorkAdd.html";
    }
    /**
     * @param id
    	 * @param model
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/4/20 16:32
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        WeekWorkMain main = weekWorkMainService.getById(id);
        List<WeekWorkDetail> detailList = weekWorkDetailService.getDetailListById(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if(currentUser.getAccount().equals("ganchaoyang")){
            model.addAttribute("isHead",1);
        }else{
            model.addAttribute("isHead","");
        }
        String createDate = sdf.format(main.getCreateTime());
        model.addAttribute("detailList",detailList);
        model.addAttribute("main",main);
        model.addAttribute("createDate",createDate);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        return PREFIX + "/weekWorkDetail.html";
    }
    /**
     * @param createUserName
    	 * @param deptName
    	 * @param timeLimit
     * @return java.lang.Object
     * @author mcrkw
     * @date 2021/4/19 16:05
     * 查询列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String createUserName,@RequestParam(required = false) String deptName,
                       @RequestParam(required = false) String timeLimit) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        WeekWorkMain main = new WeekWorkMain();
        main.setCreateUserName(createUserName);
        main.setDeptName(deptName);
        Page<Map<String, Object>> list = weekWorkMainService.selectList(main, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }
    /**
     * @param 
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     * @author mcrkw
     * @date 2021/4/29 10:27
     */
    @RequestMapping(value = "/checkAdd")
    @ResponseBody
    public  ResponseData checkAdd(){
        boolean isAdd = weekWorkMainService.checkAdd();
        if(isAdd){
            return ResponseData.error("当前周进度已经添加过了！");
        }
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/checkAndDel")
    @ResponseBody
    public  ResponseData checkAndDel(String ids){
        boolean isDel = weekWorkMainService.checkDel(ids);
        if(!isDel){
            return ResponseData.error("只能删除当前周的数据！");
        }
        return SUCCESS_TIP;
    }
    /**
     * @param weekWorkIngJson
    	 * @param weekWorkStopJson
    	 * @param weekWorkGJJson
    	 * @param weekWorkFinishJson
    	 * @param weekWorkJHGJJson
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     * @author mcrkw
     * @date 2021/4/20 14:52
     * 保存
     */
    @RequestMapping(value = "/saveWeekWork")
    @ResponseBody
    public ResponseData saveWeekWork(@RequestParam("remark") String remark,@RequestParam("weekWorkIngJson") String weekWorkIngJson,@RequestParam("weekWorkStopJson") String weekWorkStopJson,
                                     @RequestParam("weekWorkGJJson") String weekWorkGJJson,@RequestParam("weekWorkFinishJson") String weekWorkFinishJson,
                                     @RequestParam("weekWorkJHGJJson") String weekWorkJHGJJson,@RequestParam("weekWorkPXJson") String weekWorkPXJson,@RequestParam("weekWorkHYJson") String weekWorkHYJson) {
        this.weekWorkMainService.saveWeekWork(remark,weekWorkIngJson,weekWorkStopJson,weekWorkGJJson,weekWorkFinishJson,weekWorkJHGJJson,weekWorkPXJson,weekWorkHYJson);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/saveWeekWorkDetail")
    @ResponseBody
    public ResponseData saveWeekWorkDetail(@RequestParam("id") String id,@RequestParam("instruction") String instruction) {
        this.weekWorkDetailService.saveWeekWorkDetail(id,instruction);
        return SUCCESS_TIP;
    }
    /**
     * @param response
     * @return java.lang.String
     * @author mcrkw
     * @date 2021/4/22 8:52
     */
    @PostMapping("/exportToExcel")
    @ResponseBody
    public String exportToExcel(HttpServletResponse response, @RequestParam("id") Long id)throws Exception {
        return weekWorkMainService.exportToExcel(response,id);
    }
    /**
     * @param multipartFile
    	 * @param request
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     * @author mcrkw
     * @date 2021/4/28 16:34
     */
    @RequestMapping("/uploadExcel")
    @ResponseBody
    public ResponseData uploadExcel(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {

        String name = multipartFile.getOriginalFilename();
        request.getSession().setAttribute("upFile", name);
        String fileSavePath = ConstantsContext.getFileUploadPath();
        boolean isAdd = weekWorkMainService.checkAdd();
        if(isAdd){
            return ResponseData.error("当前周进度已经添加过了！");
        }
        try {
            multipartFile.transferTo(new File(fileSavePath + name));

            File file = new File(fileSavePath + name);
            try {
                ImportParams params = new ImportParams();
                params.setTitleRows(2);
                params.setHeadRows(1);
                List result = ExcelImportUtil.importExcel(file, WeekWorkDetailExcelParam.class, params);
                if (result==null || result.size()==0){
                    return ResponseData.error("上传数据不能为空");
                }
                if (result.size()>=1000){
                    return ResponseData.error("单次最多上传1000条");
                }
                String retMsg = weekWorkMainService.uploadExcel(result);

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
}
