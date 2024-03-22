package com.land.modular.landinfo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.mapper.LandDetailDao;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("landDetailService")
public class LandDetailServiceImpl  extends ServiceImpl<LandDetailDao, LandDetailInfo> implements LandDetailService {
    /**
     * 根据条件查询列表数据
     * @param vo
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Map<String, Object>> selectList(LandDetailInfoVo vo, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        //vo.setDeptId(currentUser.getDeptId());
        return this.baseMapper.selectListByPage(page, vo, beginTime, endTime);
    }
}
