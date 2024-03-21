package com.land.modular.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.file.entity.FileReceiveInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FileReceiveInfoMapper extends BaseMapper<FileReceiveInfo> {
    /**
     * 根据分享id删除
     * @param shareId
     */
    void deleteByShareId(@Param("shareId") Long shareId);

    void updateByShareId(@Param("shareId") Long shareId);

    Page<Map<String, Object>> selectReceiveList(@Param("page") Page page,
                                                @Param("shareTitle") String shareTitle,
                                                @Param("beginTime") String beginTime,
                                                @Param("endTime") String endTime,
                                                @Param("userId") Long userId);

    /**
     * 根据分享ID获取接收信息列表
     * @param shareId
     * @return
     */
    List<FileReceiveInfo> getListByShareId(@Param("shareId")  Long shareId);
   /**
    * @param userId
    * @return int
    * @author mcrkw
    * @date 2021/4/16 10:48
    */
    Integer getNewFileNum(@Param("userId")Long userId);
    /**
     * @param userId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author mcrkw
     * @date 2021/4/16 14:38
     */
    List<Map<String, Object>> getReciveFileStatistics(@Param("userId")Long userId);
    /**
     * @param userId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author mcrkw
     * @date 2021/4/16 14:46
     */
    Map<String, Object> getSendFileStatistics(@Param("userId") Long userId);
}
