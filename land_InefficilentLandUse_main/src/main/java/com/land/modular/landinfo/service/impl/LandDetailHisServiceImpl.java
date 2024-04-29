package com.land.modular.landinfo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.modular.landinfo.entity.LandDetailInfoHis;
import com.land.modular.landinfo.mapper.LandDetailHisDao;
import com.land.modular.landinfo.service.LandDetailHisService;
import org.springframework.stereotype.Service;

@Service("landDetailHisService")
public class LandDetailHisServiceImpl  extends ServiceImpl<LandDetailHisDao, LandDetailInfoHis> implements LandDetailHisService {
    @Override
    public void saveHis(LandDetailInfoHis his) {
        this.baseMapper.insert(his);
    }
}
