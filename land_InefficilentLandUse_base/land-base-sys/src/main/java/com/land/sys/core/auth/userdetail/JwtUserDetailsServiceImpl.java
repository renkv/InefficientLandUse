package com.land.sys.core.auth.userdetail;

import com.land.auth.model.LoginUser;
import com.land.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情信息获取
 *
 * @author fengshuonan
 * @Date 2019-09-28 14:07
 */
@Service("jwtUserDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthService authService;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return authService.user(username);
    }
}
