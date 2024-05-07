package com.land.modular.policy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.policy.entity.SysPolicyInfoEntity;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PolicyDao extends BaseMapper<SysPolicyInfoEntity> {
    Page<Map<String, Object>> selectListByPage(@Param("page")Page page, @Param("vo")SysPolicyInfoVo vo, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    SysPolicyInfoVo getDetailById(@Param("fileId")Long fileId);
}
