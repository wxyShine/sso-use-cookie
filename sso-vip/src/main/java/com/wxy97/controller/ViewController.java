package com.wxy97.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Resource
    private RestTemplate restTemplate;

    private final String LOGIN_INFO_URL = "http://localhost:9000/login/info?token=";


    @GetMapping("/index")
    public String toIndex(@CookieValue(value = "TOKEN",required = false) Cookie cookie, HttpSession session){
        if (cookie != null){
            String token = cookie.getValue();
            if (!StringUtils.isEmpty(token)){
                Map map = restTemplate.getForObject(LOGIN_INFO_URL + token, Map.class);
                session.setAttribute("loginUser",map);
            }
        }
        return "index";
    }
}
