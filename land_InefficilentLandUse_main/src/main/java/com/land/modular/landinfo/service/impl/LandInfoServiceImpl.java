package com.land.modular.landinfo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.exception.enums.CoreExceptionEnum;
import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.consts.ConstantsContext;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.mapper.LandInfoDao;
import com.land.modular.landinfo.service.LandInfoService;
import com.land.sys.modular.system.entity.Dept;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.service.DeptService;
import com.land.sys.modular.system.service.UserService;
import com.land.utils.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 地块信息(LandInfo)表服务实现类
 *
 * @author makejava
 * @since 2021-06-23 16:11:42
 */
@Service("landInfoService")
public class LandInfoServiceImpl  extends ServiceImpl<LandInfoDao, LandInfo> implements LandInfoService{
    @Resource
    private LandInfoDao landInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    @Override
    public LandInfo queryById(Long id) {
        return this.landInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<LandInfo> queryAllByLimit(int offset, int limit) {
        return this.landInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param landInfo 实例对象
     * @return 实例对象
     */
    @Override
    public LandInfo insert(LandInfo landInfo) {
        this.landInfoDao.insert(landInfo);
        return landInfo;
    }

    /**
     * 修改数据
     *
     * @param landInfo 实例对象
     * @return 实例对象
     */
    @Override
    public LandInfo update(LandInfo landInfo) {
        this.landInfoDao.update(landInfo);
        return this.queryById(landInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById() {
        return this.landInfoDao.deleteById() > 0;
    }
    /**
     * @param main
    	 * @param beginTime
    	 * @param endTime
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @author mcrkw
     * @date 2021/6/23 17:43
     */
    @Override
    public Page<Map<String, Object>> selectList(LandInfo main, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        main.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, main, beginTime, endTime);
    }
    /**
     * @param landInfo
     * @return void
     * @author mcrkw
     * @date 2021/6/24 10:58
     * 保存低效用地项目信息
     */
    @Override
    public void saveLandInfo(LandInfo landInfo) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        User user = userService.getById(currentUser.getId());
        landInfo.setIsDelete(0);
        if(StringUtils.isEmpty(landInfo.getId())){
            String nowDate = DateUtil.format(new Date(),"yyyyMMddHHmmss");
            landInfo.setLandNo("L"+nowDate);
            landInfo.setCreateUser(currentUser.getId());
            landInfo.setCreateUserName(user.getName());
            Dept dept = deptService.getById(user.getDeptId());
            landInfo.setDeptId(user.getDeptId());
            landInfo.setDeptName(dept.getFullName());
        }else{
            landInfo.setUpdateUser(currentUser.getId());
            landInfo.setUpdateUserName(user.getName());
        }
        this.saveOrUpdate(landInfo);
    }
    /** 删除
     * @param ids
     * @return boolean
     * @author mcrkw
     * @date 2021/6/24 11:17
     */
    @Override
    public boolean delete(String ids) {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        if (currentUser == null) {
            throw new ServiceException(CoreExceptionEnum.NO_CURRENT_USER);
        }
        User user = userService.getById(currentUser.getId());
        String[] list = ids.split(",");
        for(String s : list){
            LandInfo landInfo = this.baseMapper.selectById(s);
            landInfo.setIsDelete(1);
            landInfo.setUpdateUser(currentUser.getId());
            landInfo.setUpdateUserName(user.getName());
            this.saveOrUpdate(landInfo);
        }
        return true;
    }

    @Override
    public String exportToExcel(HttpServletResponse response, LandInfo main) throws Exception  {
        List<LandInfo> list = this.baseMapper.selectByList(main);
        //region 模板
        String exportExcelTemplate = (String) ConstantsContext.getExportExcelTemplate();
        File resource = ResourceUtils.getFile(exportExcelTemplate + "excel_landinfo.xls"); //这种方法在linux下无法工作
        TemplateExportParams params = new TemplateExportParams(resource.getPath());

//            TemplateExportParams params = new TemplateExportParams("doc/excel_chubeiqichu.xls");
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list != null && list.size() > 0) {
            /*for(LandInfo detail : list){

            }*/
        }else {
            LandInfo resp = LandInfo.class.newInstance();
            list.add(resp);
        }
        map.put("list", list);
        if (!StringUtils.isEmpty(main.getDeptName())) {
            map.put("deptName", main.getDeptName());
        }

       // map.put("createDate", sf.format(main.getCreateTime()));
        //map.put("mainRemark", main.getRemark());

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        // 判断数据
        if (workbook == null) {
            return "导出失败";
        }
        // 设置excel的文件名称
        // 重置响应对象
        response.reset();
        String fileName = URLEncoder.encode("低效用地信息表","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ fileName + ".xls");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 写出数据输出流到页面
        try {
            OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
            bufferedOutPut.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //endregion

        return "导出成功";
    }
    /**
     * @param file
     * @return cn.stylefeng.roses.kernel.model.response.ResponseData
     * @author mcrkw
     * @date 2021/7/1 15:32
     * 上传文件信息
     */
    @Override
    public ResponseData areaUpload(MultipartFile file) {
        ResponseData data = new ResponseData();
        //获取文件后缀
        String fileSuffix = ToolUtil.getFileSuffix(file.getOriginalFilename());
        if(!fileSuffix.equalsIgnoreCase("txt")){
            return ResponseData.error("文件格式不正确，请上传txt格式文件");
        }
        String fileSavePath = ConstantsContext.getFileUploadPath();
        String nowDate = DateUtil.formatDate(new Date());
        fileSavePath = fileSavePath+nowDate+"/";

        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //生成文件的唯一id
        String fileId = IdWorker.getIdStr();
        //生成文件的最终名称
        String finalName = fileId + "." + fileSuffix;
        try {
            //保存文件到指定目录
            File newFile = new File(fileSavePath + finalName);
            //创建父目录
            FileUtil.mkParentDirs(newFile);
            //保存文件
            file.transferTo(newFile);
            String encoding="GBK";
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(newFile),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            List<Map<String,Object>> dkList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            String numStr = "";
            StringBuffer stringBuffer = new StringBuffer();
            double area = 0;
            double oriArea = 0;
            while((lineTxt = bufferedReader.readLine()) != null){
                // System.out.println(lineTxt);
                stringBuffer.append(lineTxt);
                if(lineTxt.contains("=")){

                }else if(lineTxt.contains(",")){
                    String[] tmp = lineTxt.split(",");    //根据','将每行数据拆分成一个数组
                    if(tmp.length > 0){
                        if(tmp[0].startsWith("J")){
                            Map<String,Object> dkMap = (Map<String, Object>) map.get(numStr);
                            List<Tuple> list;
                            if(tmp[1].equals("1")){
                                list = (List<Tuple>) dkMap.get("1");
                            }else{
                                boolean contains=dkMap.containsKey(tmp[1]);
                                if(contains){
                                    list = (List<Tuple>) dkMap.get(tmp[1]);
                                }else{
                                    list = new ArrayList<>();
                                }
                            }
                            String yStr = tmp[2];
                            String xStr = tmp[3].substring(2);
                            Tuple tuple = new Tuple();
                            tuple.setX(Double.parseDouble(xStr));
                            tuple.setY(Double.parseDouble(yStr));
                            list.add(tuple);
                            dkMap.put(tmp[1],list);
                        }else{
                            numStr = tmp[2];
                            List<Tuple> list = new ArrayList<>();
                            Map<String,Object> dkMap = new HashMap<>();
                            dkMap.put("1",list);
                            dkMap.put("area",tmp[1]);
                            oriArea = DoubleUtils.add(oriArea,Double.valueOf(tmp[1]));
                            map.put(numStr,dkMap);
                        }
                    }
                }
            }
            System.out.println(map.size());
            for (String key : map.keySet()) {
                Map<String,Object> m = (Map<String, Object>) map.get(key);
                for (String k : m.keySet()) {
                    if(!k.equalsIgnoreCase("area")){
                        List<Tuple> list = (List<Tuple>) m.get(k);
                        double result = AreaUtil.getArea(list);
                        if(k.equals("1")){
                            area = DoubleUtils.add(area,result);
                            System.out.println("面积=" + result);
                        }else{
                            area = DoubleUtils.sub(area,result);
                            System.out.println("key=" + key + ", value=" + map.get(key));
                        }
                    }
                }
                System.out.println("key=" + key + ", value=" + map.get(key));
            }
            //判断误差是否大于1平米
            String compare = DoubleUtils.compare(1,Math.abs(DoubleUtils.sub(DoubleUtils.divide(area,10000.0),oriArea)));
            if(compare.equals("-1")){
                return ResponseData.error("面积差大于1平米，请修改坐标后重新上传");
            }
            Map<String,Object> reMap = new HashMap<>();
            reMap.put("area",DoubleUtils.divide(area,10000.0));
            reMap.put("originalFilename",originalFilename);
            reMap.put("areaInfo",stringBuffer.toString());
            data.setData(reMap);
            read.close();
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return ResponseData.error("读取文件内容出错");
        }
        return data;
    }
}
