package com.land.modular.weekwork.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.modular.weekwork.entity.WeekWorkDetail;
import com.land.modular.weekwork.mapper.WeekWorkDetailMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mcrkw
 * @date 2021年04月20日 15:58
 */
@Service
public class WeekWorkDetailService extends ServiceImpl<WeekWorkDetailMapper, WeekWorkDetail> {
    /**
     * @param mainId
     * @return java.util.List<com.land.modular.weekwork.entity.WeekWorkDetail>
     * @author mcrkw
     * @date 2021/4/20 16:34
     * 根据主表id获取详情列表
     */
    public List<WeekWorkDetail> getDetailListById(Long mainId) {
        List<WeekWorkDetail> list = this.baseMapper.getDetailListById(mainId);
        return list;
    }

    /*
     * @param id
     * @return void
     * @author mcrkw
     * @date 2021/5/17 14:53
     * 根据mainId删除详情
     */
    public void deleteByMainId(Long id) {
        this.baseMapper.deleteByMainId(id);
    }
    /**
     * @param id
    	 * @param instruction
     * @return void
     * @author mcrkw
     * @date 2021/5/24 9:01
     * 保存领导批示
     */
    public void saveWeekWorkDetail(String id, String instruction) {

        WeekWorkDetail detail = this.getById(id);
        detail.setInstruction(instruction);
        detail.setInstructionTime(new Date());
        this.saveOrUpdate(detail);
    }
}
