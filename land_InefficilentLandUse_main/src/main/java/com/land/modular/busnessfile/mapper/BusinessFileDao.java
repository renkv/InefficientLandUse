package com.land.modular.busnessfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.busnessfile.entity.BusinessFileInfoEntity;
import com.land.modular.busnessfile.vo.BusinessFileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface BusinessFileDao extends BaseMapper<BusinessFileInfoEntity> {
    Page<Map<String, Object>> selectListByPage(@Param("page") Page page, @Param("vo")BusinessFileVo vo);

    List<Map<String, Object>> getDistinctYear();
}
