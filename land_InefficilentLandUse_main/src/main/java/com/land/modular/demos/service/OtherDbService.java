package com.land.modular.demos.service;

import cn.hutool.core.util.RandomUtil;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.mapper.UserMapper;
import com.land.sys.modular.system.service.UserService;
import cn.stylefeng.roses.core.mutidatasource.annotion.DataSource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
@Service
public class OtherDbService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserService userService;

    @DataSource(name = "test")
    public void otherdb() {
        User user = new User();
        user.setAccount(RandomUtil.randomString(5));
        user.setPassword(RandomUtil.randomString(5));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setCreateUser(1L);
        user.setUpdateUser(1L);
        userService.save(user);
    }

}
