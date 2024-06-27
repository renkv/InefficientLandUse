package com.land.modular.busnessfile.controller;

import cn.hutool.http.HttpResponse;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.base.log.BussinessLog;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.busnessfile.service.BusinessFileService;
import com.land.modular.busnessfile.vo.BusinessFileVo;
import com.land.modular.busnessfile.entity.BusinessFileInfoEntity;
import com.land.sys.core.constant.dictmap.DeptDict;
import com.land.sys.modular.system.warpper.UserWrapper;
import com.land.utils.ExcelToPdfUtil;
import com.land.utils.FileCopyUtil;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * 低效企业工作资料
 */
@Controller
@RequestMapping("/busfile")
public class BusinessFileController extends BaseController {
    private String PREFIX = "/busfile";
    @Autowired
    private BusinessFileService businessFileService;
    /**
     * 低效企业工作资料首页
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String main( Model model) {
        List<Map<String,Object>> yearList = businessFileService.getDistinctYear();
        model.addAttribute("yearList",yearList);
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
     * @return
     */
    @RequestMapping("/selectList")
    @ResponseBody
    public Object selectList(BusinessFileVo vo) {
        Page<Map<String, Object>> list = businessFileService.selectList(vo);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     *
     * @param file
     * @param vo
     * @return
     */
    @BussinessLog(value = "保存低效企业工作资料信息", key = "id", dict = DeptDict.class)
    @RequestMapping(value = "/saveFieInfo")
    @ResponseBody
    public ResponseData saveFieInfo(@RequestPart("file") MultipartFile file, BusinessFileVo vo) {
        return businessFileService.saveFieInfo(file,vo);
    }

    /**
     * 删除信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public  ResponseData delete(String ids){
        boolean isDel = businessFileService.delete(ids);
        return SUCCESS_TIP;
    }
    /**
     * 查看文件
     * @param model
     * @return
     */
    @RequestMapping("/showExcel")
    public  String showExcel(Model model,@RequestParam(required = false)String id){
        BusinessFileInfoEntity entity = businessFileService.selectById(id);
        String url = "";
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
            try {
                FileCopyUtil.copy(file,targetPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            url = "/assets/tmp/"+file.getName().replaceAll("[\\\\/]", "");
        }
        model.addAttribute("entity",entity);
        model.addAttribute("url",url);
        return PREFIX + "/previewExcel.html";
    }

    /**
     * 直接下载文件
     * @param id
     * @return
     */
    @RequestMapping("/download")
    public void downloadFile(HttpServletResponse response, String id){
        BusinessFileInfoEntity entity = businessFileService.selectById(id);
        //获取文件路径
        String filePath = entity.getFilePath();
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ entity.getFileName());
            byte[] buffer = new byte[1024];
            int len;
            while ((len  = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, len);
            }
        } catch (IOException e) {
            e. printStackTrace();
        }
    }

    /**
     * 查看文件
     * @param model
     * @return
     */
    @RequestMapping("/showPre")
    public  String showPre(Model model,@RequestParam(required = false)String id){
        BusinessFileInfoEntity entity = businessFileService.selectById(id);
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
            }else if(entity.getFileSuffix().toLowerCase(Locale.ROOT).contains("xls")){
                String uid = String.valueOf(UUID.randomUUID());
                String newuid = String.valueOf(UUID.randomUUID());
                url = "/assets/tmp/" + newuid + ".pdf";
                //url = "/assets/tmp/" + uid + ".pdf";
                try {
                    ExcelToPdfUtil.excelToPdf(entity.getFilePath(),targetPath+"\\" + uid + ".pdf",targetPath+"\\" + newuid + ".pdf");

                    //fileZh(targetPath+"\\" + uid + ".pdf", targetPath+"\\" + newuid + ".pdf");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
     * 去除第一行水印
     * @param filePath 原路径
     * @param filePathS 新路径
     */
    public void fileZh(String filePath,String filePathS) throws IOException {

        //将文档输入input流
        InputStream is = new FileInputStream(filePath);
        XWPFDocument document = new XWPFDocument(is);
        //Spire的产品 生成的文件会自带同步警告信息，这里来删除Spire产品的的警告信息
        document.removeBodyElement(0);
        //将文件流put到新的文件中
        OutputStream os=new FileOutputStream(filePathS);
        try {
            document.write(os);
            //log.info("水印去除完成，文档生成成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
