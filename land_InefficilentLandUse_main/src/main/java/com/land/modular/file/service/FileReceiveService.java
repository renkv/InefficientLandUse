package com.land.modular.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.model.LoginUser;
import com.land.base.pojo.page.LayuiPageFactory;
import com.land.modular.file.entity.FileReceiveInfo;
import com.land.modular.file.mapper.FileReceiveInfoMapper;
import com.land.modular.file.vo.FileShareInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FileReceiveService extends ServiceImpl<FileReceiveInfoMapper, FileReceiveInfo> {
    @Autowired
    private FileShareService fileShareService;
    /**
     * 根据分享id删除
     * @param shareId
     */
    public void deleteByShareId( Long shareId) {
        this.baseMapper.deleteByShareId(shareId);
    }

    /**
     * 根据
     * @param shareId
     */
    public void updateByShareId(Long shareId) {
        this.baseMapper.updateByShareId(shareId);
    }

    /**
     *
     * @param shareTitle
     * @param beginTime
     * @param endTime
     * @return
     */
    public Page<Map<String, Object>> selectReceiveList(String shareTitle, String beginTime, String endTime) {
        Page page = LayuiPageFactory.defaultPage();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        return this.baseMapper.selectReceiveList(page, shareTitle, beginTime, endTime,currentUser.getId());
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FileShareInfoVo getDetailById(Long id) {
        FileReceiveInfo receiveInfo = this.getById(id);
        if(null != receiveInfo && receiveInfo.getIsRead().equals("0")){
            receiveInfo.setIsRead("1");
            this.saveOrUpdate(receiveInfo);

        }
        FileShareInfoVo fileShareInfoVo = fileShareService.getDetailById(receiveInfo.getShareId());
        fileShareInfoVo.setReply(receiveInfo.getReplyInfo());
        return fileShareInfoVo;
    }


    /**
     * 保存回复信息
     * @param replyInfo
     * @param id
     */
    public void saveFileReply(String replyInfo, Long id) {
        FileReceiveInfo receiveInfo = this.getById(id);
        receiveInfo.setIsReply("1");
        receiveInfo.setReplyInfo(replyInfo);
        receiveInfo.setReplyTime(new Date());
        this.saveOrUpdate(receiveInfo);
    }

    /**
     * 接收文件列表
     * @param shareId
     * @return
     */
    public List<FileReceiveInfo> getListByShareId(Long shareId) {
        List<FileReceiveInfo> list = this.baseMapper.getListByShareId(shareId);
        return list;
    }
    /**
     *
     * @author mcrkw
     * @date 2021/4/16 10:40
     * @return int
     */
    public int getNewFileNum() {
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        int num = this.baseMapper.getNewFileNum(currentUser.getId());
        return num;
    }
    /**
     * 获取首页统计信息
     * @return 
     * @author mcrkw
     * @date 2021/4/16 14:35
     */
    public HashMap<String, Object> getFileStatistics() {
        HashMap<String, Object> map = new HashMap<>();
        LoginUser currentUser = LoginContextHolder.getContext().getUser();
        //接收文件数量
        List<Map<String, Object>> receiveMapList = this.baseMapper.getReciveFileStatistics(currentUser.getId());
        List<Object> monthList = new ArrayList<>();
        List<Object> monthNumList = new ArrayList<>();
        for(Map<String, Object> receiveMap : receiveMapList){
            Iterator it = receiveMap.entrySet().iterator();

            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                String key =  entry.getKey().toString();//返回与此项对应的键
                Object ob = entry.getValue();// 返回与此项对应的值
                if("yearMonth".equals(key)){
                    monthList.add(ob);
                }else{
                    monthNumList.add(ob);
                }
            }
        }
        map.put("monthList",monthList);
        map.put("monthNumList",monthNumList);
        //发送文件数量
        Map<String, Object> sendMap = this.baseMapper.getSendFileStatistics(currentUser.getId());
        Iterator it2 = sendMap.entrySet().iterator();
        List<Object> userList = new ArrayList<>();
        List<Object> userNumList = new ArrayList<>();
        while(it2.hasNext()){
            Map.Entry entry = (Map.Entry)it2.next();
            String key =  entry.getKey().toString();//返回与此项对应的键
            Object ob = entry.getValue();// 返回与此项对应的值
            if("receive_user_name".equals(key)){
                userList.add(ob);
            }else{
                userNumList.add(ob);
            }
        }
        map.put("userList",userList);
        map.put("userNumList",userNumList);
        return map;
    }
}
