package com.land.modular.weekwork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WeekWorkDetailMapper extends BaseMapper<WeekWorkDetail> {
    List<WeekWorkDetail> getDetailListById(@Param("mainId") Long mainId);

    void deleteByMainId(@Param("mainId") Long mainId);
}
