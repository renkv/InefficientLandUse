package com.land.modular.landinfo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.landinfo.entity.LandDetailInfo;
import com.land.modular.landinfo.mapper.LandDetailDao;
import com.land.modular.landinfo.service.LandDetailService;
import com.land.modular.landinfo.vo.LandDetailExcelParam;
import com.land.modular.landinfo.vo.LandDetailInfoVo;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import com.land.modular.weekwork.entity.WeekWorkMain;
import com.land.modular.weekwork.vo.WeekWorkDetailExcelParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /**
     * 批量导入数据
     * @param result
     * @return
     */
    @Override
    public String uploadExcel(List result) {
        String msg = "";
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        Calendar calendar = Calendar.getInstance();
        // 获取当前年
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月
        int month = calendar.get(Calendar.MONTH) + 1;
        Date now = new Date();

        for (int i = 0; i < result.size(); i++) {
            LandDetailExcelParam param = (LandDetailExcelParam) result.get(i);
            LandDetailInfo main = new LandDetailInfo();
            BeanUtils.copyProperties(param,main);
            main.setCreateUser(currentUser.getId());
            main.setCreateUserName(currentUser.getName());
            main.setCreateTime(new Date());
            this.saveOrUpdate(main);
        }
        return msg;
    }
}
