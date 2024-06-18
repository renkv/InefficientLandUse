/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.land.sys.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.land.auth.context.LoginContextHolder;
import com.land.auth.exception.AuthException;
import com.land.auth.exception.enums.AuthExceptionEnum;
import com.land.auth.jwt.JwtTokenUtil;
import com.land.auth.jwt.payload.JwtPayLoad;
import com.land.auth.model.LoginUser;
import com.land.auth.service.AuthService;
import com.land.sys.core.properties.BeetlProperties;
import com.land.sys.core.properties.GunsProperties;
import com.land.sys.core.util.Base64Util;
import com.land.sys.core.util.DefaultImages;
import com.land.sys.core.util.HttpClient;
import com.land.sys.core.util.SaltUtil;
import com.land.sys.modular.consts.entity.SysConfig;
import com.land.sys.modular.consts.model.params.SysConfigParam;
import com.land.sys.modular.consts.model.result.SysConfigResult;
import com.land.sys.modular.consts.service.SysConfigService;
import com.land.sys.modular.rest.factory.DeptFactory;
import com.land.sys.modular.system.entity.Dept;
import com.land.sys.modular.system.entity.Role;
import com.land.sys.modular.system.entity.User;
import com.land.sys.modular.system.factory.UserFactory;
import com.land.sys.modular.system.model.UserDto;
import com.land.sys.modular.system.service.DeptService;
import com.land.sys.modular.system.service.RoleService;
import com.land.sys.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import io.jsonwebtoken.Claims;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import static com.land.base.consts.ConstantsContext.getJwtSecretExpireSec;
import static com.land.base.consts.ConstantsContext.getTokenHeaderName;

/**
 * 单点登录控制器
 *
 * @author fengshuonan
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
public class SsoLoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 单点登录入口
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/ssoLogin", method = RequestMethod.GET)
    public String ssoLogin(@RequestParam("token") String token, HttpServletResponse response, Model model) {
        GunsProperties properties = new GunsProperties();
        String url = properties.TOKENURL;
        System.out.println(token);
        String result = HttpClient.doPost(url,"token="+token,token);
        System.out.println(result);
        //result = "{\"msg\":\"操作成功\", \"code\":200, \"data\":{\"userId\":128, \"userName\":\"桥西区\", \"userArea\":\"桥西区\"}}";

        JSONObject job = JSONObject.parseObject(result);
        if(job!= null && job.get("code") != null && job.get("code").toString().equals("200")){
            JSONObject data = (JSONObject) job.get("data");
            String userName = data.get("userName").toString();
            String userArea = data.get("userArea").toString();
            UserDetails userDetails;
            User user = userService.getByAccount(userName);
            String token1;
            //获取默认密码
            SysConfigParam param = new SysConfigParam();
            param.setCode("GUNS_DEFAULT_PASSWORD");
            SysConfigResult passwordConfig = sysConfigService.findBySpec(param);
            if(user == null){
                //获取默认角色
                Role roleDef = roleService.selectOneByName("管理员");
                //根据地区创建部门
                Dept dept = new Dept();
                dept.setFullName(userArea);
                dept.setSimpleName(userArea);
                deptService.save(dept);
                UserDto user1 = new UserDto();
                user1.setAccount(userName);
                if(passwordConfig != null){
                    user1.setPassword(passwordConfig.getValue());

                }else{
                    user1.setPassword("123456");
                }
                if(roleDef != null){
                    user1.setRoleId(roleDef.getRoleId().toString());
                }else {
                    user1.setRoleId("1782284693593341954");
                }
                user1.setDeptId(dept.getDeptId());
                // 完善账号信息
                String salt = SaltUtil.getRandomSalt();
                String password = SaltUtil.md5Encrypt(user1.getPassword(), salt);

                User newUser = UserFactory.createUser(user1, password, salt);
                userService.save(newUser);
                //添加职位关联
                userService.addPosition(user1.getPosition(), newUser.getUserId());
                token1 = JwtTokenUtil.generateToken(new JwtPayLoad( newUser.getUserId(), newUser.getAccount(), "1"));
            }else{
                token1 = JwtTokenUtil.generateToken(new JwtPayLoad( user.getUserId(), user.getAccount(), "1"));
            }
            //创建token
            Cookie authorization = new Cookie(getTokenHeaderName(), token1);
            authorization.setMaxAge(getJwtSecretExpireSec().intValue());
            authorization.setHttpOnly(true);
            response.addCookie(authorization);
            try {
                userDetails = userDetailsService.loadUserByUsername(userName);
            } catch (UsernameNotFoundException e) {
                throw new AuthException(AuthExceptionEnum.LOGIN_EXPPIRED);
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //登录并创建token
            String logtoken = authService.login(userName, passwordConfig.getValue());
        }else{
            return "/login.html";
        }
        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();
        if (roleList == null || roleList.size() == 0) {
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

        List<Map<String, Object>> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);
        model.addAttribute("avatar", DefaultImages.defaultAvatarUrl());
        model.addAttribute("name", user.getName());
        //登录并创建token
        /*Claims claimFromToken = JwtTokenUtil.getClaimFromToken(token);
        System.out.println(claimFromToken);
        Long accountId = (Long) claimFromToken.get("accountId");
        System.out.println(accountId);

        //记录登录日志
        //LogManager.me().executeLog(LogTaskFactory.loginLog(Long.valueOf(accountId), getIp()));

        String token1 = JwtTokenUtil.generateToken(new JwtPayLoad(1L, "admin", "1"));

        //创建token
        Cookie authorization = new Cookie(getTokenHeaderName(), token1);
        authorization.setMaxAge(getJwtSecretExpireSec().intValue());
        authorization.setHttpOnly(true);
        response.addCookie(authorization);

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername("admin");
        } catch (UsernameNotFoundException e) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPPIRED);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //获取当前用户角色列表
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

        List<Map<String, Object>> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);
        model.addAttribute("avatar", DefaultImages.defaultAvatarUrl());
        model.addAttribute("name", user.getName());*/

        return "/index.html";
    }

    /**
     * 单点登录退出
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/ssoLogout")
    public String ssoLogout(@RequestParam("token") String token, HttpServletResponse response, Model model) {
        return null;
    }

}