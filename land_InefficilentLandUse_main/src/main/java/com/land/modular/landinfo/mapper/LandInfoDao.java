package com.land.modular.landinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandInfo;
import com.land.modular.weekwork.entity.WeekWorkMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 地块信息(LandInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-06-23 16:11:36
 */
public interface LandInfoDao extends BaseMapper<LandInfo> {

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    LandInfo queryById(@Param("id") Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<LandInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param landInfo 实例对象
     * @return 对象列表
     */
    List<LandInfo> queryAll(LandInfo landInfo);

    /**
     * 新增数据
     *
     * @param landInfo 实例对象
     * @return 影响行数
     */
    int insert(LandInfo landInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LandInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LandInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LandInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<LandInfo> entities);

    /**
     * 修改数据
     *
     * @param landInfo 实例对象
     * @return 影响行数
     */
    int update(LandInfo landInfo);

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 影响行数
     */
    int deleteById();

    Page<Map<String, Object>> selectListByPage(@Param("page")Page page, @Param("main")LandInfo main, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<LandInfo> selectByList(@Param("main")LandInfo main);
}

