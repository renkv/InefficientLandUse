package com.land.modular.busnessfile.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.busnessfile.entity.BusinessFileInfoEntity;
import com.land.modular.busnessfile.vo.BusinessFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BusinessFileService {
    Page<Map<String, Object>> selectList(BusinessFileVo vo);

    List<Map<String, Object>> getDistinctYear();

    ResponseData saveFieInfo(MultipartFile file, BusinessFileVo vo);

    boolean delete(String ids);

    BusinessFileInfoEntity selectById(String id);
}
