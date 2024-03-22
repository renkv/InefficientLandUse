package com.land.modular.landinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface LandDetailDao extends BaseMapper<LandDetailInfo> {

    Page<Map<String, Object>> selectListByPage(@Param("page")Page page, @Param("vo")LandDetailInfoVo vo, @Param("beginTime")String beginTime, @Param("endTime")String endTime);
}
