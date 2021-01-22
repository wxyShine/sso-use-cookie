package com.wxy97.controller;

import com.wxy97.pojo.User;
import com.wxy97.utils.LoginCacheUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    //模拟账号密码
    private static Set<User> dbUsers;
    static {
        dbUsers = new HashSet<>();
        dbUsers.add(new User().setId(1).setUsername("admin").setPassword("admin"));
    }

    @PostMapping
    public String doLogin(User user, HttpSession session, HttpServletResponse response){

        String target = (String) session.getAttribute("target");

        Optional<User> first = dbUsers.stream().filter(dbUser -> dbUser.getUsername().equals(user.getUsername()) &&
                dbUser.getPassword().equals(user.getPassword()))
                .findFirst();
        if (first.isPresent()){
            //保存登录信息
            String token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("TOKEN", token);
            cookie.setDomain("localhost");
            response.addCookie(cookie);
            LoginCacheUtil.loginUser.put(token,first.get());
        }else {
            session.setAttribute("msg","登录失败");
            System.out.println(target);
            return "login";
        }

        return "redirect:"+target;
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(String token){
        if (!StringUtils.isEmpty(token)){
            User user = LoginCacheUtil.loginUser.get(token);
            return ResponseEntity.ok(user);
        }else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/loginOut")
    public String loginOut(@CookieValue(value = "TOKEN")Cookie cookie,HttpServletResponse response,String target){
        cookie.setMaxAge(0);
        LoginCacheUtil.loginUser.remove(cookie.getValue());
        return "redirect:"+target;
    }
}
