package com.land.modular.plan.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.plan.vo.LandPlanInfoVo;

import java.util.List;
import java.util.Map;

public interface LandPlanInfoService {
    Page<Map<String, Object>> selectList(LandPlanInfoVo vo, String beginTime, String endTime);

    ResponseData savePlan(LandPlanInfoVo vo);

    boolean delete(String ids);

    LandPlanInfoVo getDetailById(Long id);

    String uploadExcel(List result,String planType);
}
