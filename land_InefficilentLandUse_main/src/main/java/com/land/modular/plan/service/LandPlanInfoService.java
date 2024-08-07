package com.land.modular.plan.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.plan.vo.LandPlanInfoVo;
import com.land.modular.statistics.vo.InBusinessVo;
import com.land.modular.statistics.vo.LandStaVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LandPlanInfoService {
    Page<Map<String, Object>> selectList(LandPlanInfoVo vo, String beginTime, String endTime);

    ResponseData savePlan(LandPlanInfoVo vo, LandDetailInfo landDetail);

    boolean delete(String ids);

    LandPlanInfoVo getDetailById(Long id);

    String uploadExcel(List result,String planType);

    Page<Map<String, Object>> diffStaList(LandStaVo vo, String beginTime, String endTime);

    ResponseData savePlanById(Long id, String field, String value);

    Page<Map<String, Object>> inbusList(InBusinessVo vo, String beginTime, String endTime);

    String exportToBusExcel(HttpServletResponse response, InBusinessVo vo, String beginTime, String endTime);

    List<Map<String, Object>> getDistinctYear();

    List<InBusinessVo> inbusListNoPage(InBusinessVo vo, String beginTime, String endTime);
}
