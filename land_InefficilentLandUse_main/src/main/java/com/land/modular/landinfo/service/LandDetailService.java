package com.land.modular.landinfo.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.statistics.vo.LandStaVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LandDetailService {
    Page<Map<String, Object>> selectList(LandDetailInfoVo vo, String beginTime, String endTime);

    String uploadExcel(List result,String category);

    String exportToExcel(HttpServletResponse response, LandInfo main,String beginTime, String endTime);

    LandDetailInfo saveLandDetail(LandDetailInfo landDetail);

    boolean delete(String ids);

    LandDetailInfoVo getDetailById(Long id);

    ResponseData saveLandDis(LandDetailInfo landDetail);

    Page<Map<String, Object>> landStaList(LandStaVo vo, String beginTime, String endTime);

    Page<Map<String, Object>> cycleStaList(LandStaVo vo, String beginTime, String endTime);

    void getFileStream(HttpServletResponse response,String name);

    LandDetailInfo selectByLandCode(String landCode);
}
