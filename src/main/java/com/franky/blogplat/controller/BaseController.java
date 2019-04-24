package com.franky.blogplat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Franky on 2019/4/20.
 */
@Controller
@RequestMapping
public class BaseController {

    /**
     * 首页
     * @return
     */
    @GetMapping(value = "/")
    public String bases(){
        return "bases/home";
    }

    /**
     * 注册页面
     * @return
     */
    @GetMapping(value = "/register")
    public String register(){
        return "bases/register";
    }

    /**
     * 登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login(){
        return "/login";
    }

    /**
     * 登录失败页面
     * @return
     */
    @GetMapping(value = "/login-failed")
    public String loginFailed(Model model){
        model.addAttribute("loginError", true)
                .addAttribute("errMsg", "用户名或密码有误");
        return "/login";
    }
}
