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
import com.land.modular.plan.vo.LandPlanInfoVo;
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
    public String main(String landStatus, Model model) {
        model.addAttribute("landStatus",landStatus);
        if(landStatus != null){
            if(landStatus.equals("1")){
                //收储再开发
                return PREFIX + "/storage.html";
            }else if(landStatus.equals("2")){
                //自主开发
                return PREFIX + "/autonomic.html";
            }else if(landStatus.equals("3")){
                //技术提升
                return PREFIX + "/tecimpro.html";
            }else if(landStatus.equals("4")){
                //复垦耕地
                return PREFIX + "/reclamation.html";
            }else if(landStatus.equals("5")){
                //司法处置或转让
                return PREFIX + "/justice.html";
            }
        }
        return PREFIX + "/main.html";
    }
    /**
     * 收储再开发 storage and redev
     */
    @RequestMapping("/add")
    public  String add(String landStatus,Model model){
        model.addAttribute("status",landStatus);
        if(landStatus.equals("1")){
            //收储再开发
            return PREFIX + "/storageAdd.html";
        }else if(landStatus.equals("2")){
            //自主开发
            return PREFIX + "/autonomicAdd.html";
        }else if(landStatus.equals("3")){
            //技术提升
            return PREFIX + "/tecimproAdd.html";
        }else if(landStatus.equals("4")){
            //复垦耕地
            return PREFIX + "/reclamationAdd.html";
        }else if(landStatus.equals("5")){
            //司法处置或转让
            return PREFIX + "/justiceAdd.html";
        }
        return PREFIX + "/storage.html";
    }

    /**
     * 编辑处置
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit")
    public  String edit(Long id,Model model){
        LandDetailInfoVo vo = landDetailService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        if(vo.getLandStatus().equals("1")){
            //收储再开发
            return PREFIX + "/storageEdit.html";
        }else if(vo.getLandStatus().equals("2")){
            //自主开发
            return PREFIX + "/autonomicEdit.html";
        }else if(vo.getLandStatus().equals("3")){
            //技术提升
            return PREFIX + "/tecimproEdit.html";
        }else if(vo.getLandStatus().equals("4")){
            //复垦耕地
            return PREFIX + "/reclamationEdit.html";
        }else if(vo.getLandStatus().equals("5")){
            //司法处置或转让
            return PREFIX + "/justiceEdit.html";
        }
        return PREFIX + "/storage.html";
    }

    /**
     * 详情页面
     */
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id,Model model) {
        LandDetailInfoVo vo = landDetailService.getDetailById(id);
        model.addAttribute("vo",vo);
        model.addAttribute("ctxPath", ConfigListener.getConf().get("contextPath"));
        if(vo.getLandStatus().equals("storage")) {
            if (!StringUtils.isEmpty(vo.getScStatus())) {
                if ("1".equals(vo.getScStatus())) {
                    vo.setScStatus("拟收储");
                } else if ("2".equals(vo.getScStatus())) {
                    vo.setScStatus("已收储");
                } else if ("2".equals(vo.getScStatus())) {
                    vo.setScStatus("待开发");
                } else if ("4".equals(vo.getScStatus())) {
                    vo.setScStatus("已开发");
                }
            }

        }
        if (vo.getLandStatus().equals("1")) {
            return PREFIX + "/storageDetail.html";
        } else if (vo.getLandStatus().equals("2")) {
            //自主开发
            return PREFIX + "/autonomicDetail.html";
        } else if (vo.getLandStatus().equals("3")) {
            //技术提升
            return PREFIX + "/tecimproDetail.html";
        } else if (vo.getLandStatus().equals("4")) {
            //复垦耕地
            return PREFIX + "/reclamationDetail.html";
        } else if (vo.getLandStatus().equals("5")) {
            //司法处置或转让
            return PREFIX + "/justiceDetail.html";
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
        vo.setLandStatus(status);
        vo.setCreateUserName(createUserName);
        Page<Map<String, Object>> list = landDetailService.selectList(vo, beginTime, endTime);
        Page wrapped = new UserWrapper(list).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }

    /**
     *
     * @param landDetail
     * @param id
     * @return
     */
    @BussinessLog(value = "保存低效用地项目处置信息", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/saveLandDis")
    @ResponseBody
    public ResponseData saveLandDis(@Valid LandDetailInfo landDetail,@RequestParam(required = true) Long id) {
        landDetail.setId(id);
        return landDetailService.saveLandDis(landDetail);
    }
}
