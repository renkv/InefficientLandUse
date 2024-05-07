package com.land.modular.policy.service;

import cn.stylefeng.roses.kernel.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.land.modular.policy.entity.SysPolicyInfoEntity;
import com.land.modular.policy.vo.SysPolicyInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PolicyInfoService {
    Page<Map<String, Object>> selectList(SysPolicyInfoVo vo, String beginTime, String endTime);

    ResponseData savePolicy(MultipartFile file, SysPolicyInfoVo vo);

    boolean delete(String ids);

    SysPolicyInfoEntity selectById(String fileId);

    SysPolicyInfoVo getDetailById(Long fileId);
}
