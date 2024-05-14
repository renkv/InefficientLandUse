package com.land.modular.landinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LandDetailDao extends BaseMapper<LandDetailInfo> {

    Page<Map<String, Object>> selectListByPage(@Param("page")Page page, @Param("vo")LandDetailInfoVo vo, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<LandDetailInfo> selectByList(@Param("vo")LandInfo main, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    LandDetailInfoVo getDetailById(@Param("id")Long id);

    LandDetailInfo getByYearAndQx(@Param("year")int nowYear, @Param("xdm")String xdm);

    LandDetailInfo getOneById(@Param("id")Long id);
}
