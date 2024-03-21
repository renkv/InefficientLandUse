package com.land.modular.landinfo.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.landinfo.entity.LandInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 地块信息(LandInfo)表服务接口
 *
 * @author makejava
 * @since 2021-06-23 16:11:39
 */
public interface LandInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param 主键
     * @return 实例对象
     */
    LandInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<LandInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param landInfo 实例对象
     * @return 实例对象
     */
    LandInfo insert(LandInfo landInfo);

    /**
     * 修改数据
     *
     * @param landInfo 实例对象
     * @return 实例对象
     */
    LandInfo update(LandInfo landInfo);

    /**
     * 通过主键删除数据
     *
     * @param 主键
     * @return 是否成功
     */
    boolean deleteById();
    /**
     * @param main
    	 * @param beginTime
    	 * @param endTime
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<java.lang.String,java.lang.Object>>
     * @author mcrkw
     * @date 2021/6/23 17:46
     */
    Page<Map<String, Object>> selectList(LandInfo main, String beginTime, String endTime);

    void saveLandInfo(LandInfo landInfo);

    boolean delete(String ids);

    String exportToExcel(HttpServletResponse response, LandInfo main) throws Exception ;

    ResponseData areaUpload(MultipartFile file);
}
