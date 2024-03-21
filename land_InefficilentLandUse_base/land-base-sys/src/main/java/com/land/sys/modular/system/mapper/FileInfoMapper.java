package com.land.sys.modular.system.mapper;

import com.land.sys.modular.system.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件信息表
 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    List<FileInfo> getListByBusinessKey(@Param("businessKey") String businessKey);
}
