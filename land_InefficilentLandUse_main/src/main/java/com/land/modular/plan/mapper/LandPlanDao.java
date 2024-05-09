package com.land.modular.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.plan.entity.LandPlanInfoEntity;
import com.land.modular.plan.vo.LandPlanInfoVo;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface LandPlanDao extends BaseMapper<LandPlanInfoEntity> {
    Page<Map<String, Object>> selectListByPage(@Param("page")Page page,@Param("vo") LandPlanInfoVo vo, @Param("beginTime")String beginTime,@Param("endTime") String endTime);

    LandPlanInfoVo getDetailById(@Param("id")Long id);
}