package com.land.modular.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.file.entity.FileShareInfo;
import com.land.modular.file.vo.FileShareInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 发送文件接口
 */
public interface FileShareInfoMapper extends BaseMapper<FileShareInfo> {
    /**
     * 查询文件分享列表
     * @param page
     * @param info
     * @param beginTime
     * @param endTime
     * @return
     */
    Page<Map<String, Object>> selectShareList(@Param("page") Page page,
                                              @Param("info") FileShareInfo info,
                                              @Param("beginTime") String beginTime,
                                              @Param("endTime") String endTime,
                                              @Param("userId") Long userId);

    /**
     * 查询文件分享详情
     * @param shareId
     * @return
     */
    FileShareInfoVo getDetailById(@Param("shareId") Long shareId);
}
