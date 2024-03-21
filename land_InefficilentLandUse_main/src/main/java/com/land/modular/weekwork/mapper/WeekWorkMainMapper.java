package com.land.modular.weekwork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.weekwork.entity.WeekWorkMain;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author mcrkw
 * @date 2021年04月19日 16:09
 */
public interface WeekWorkMainMapper extends BaseMapper<WeekWorkMain> {
    /**
     * @param page
    	 * @param main
    	 * @param beginTime
    	 * @param endTime
    	 * @param userId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @author mcrkw
     * @date 2021/4/19 16:12
     * 分页查询列表
     */
    Page<Map<String, Object>> selectListByPage(@Param("page") Page page,@Param("main")  WeekWorkMain main,@Param("beginTime")  String beginTime,
                                         @Param("endTime") String endTime, @Param("userId") Long userId, @Param("deptId") Long deptId);

    Integer getCount(@Param("main") WeekWorkMain main);
}
