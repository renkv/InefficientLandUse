package com.land.modular.landinfo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.vo.LandDetailInfoVo;

import java.util.List;
import java.util.Map;

public interface LandDetailService {
    Page<Map<String, Object>> selectList(LandDetailInfoVo vo, String beginTime, String endTime);

    String uploadExcel(List result);
}
