package com.franky.blogplat.controller;

import com.franky.blogplat.domain.*;
import com.franky.blogplat.service.AuthorityService;
import com.franky.blogplat.service.BlogService;
import com.franky.blogplat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Franky on 2019/4/20.
 */
@Controller
@RequestMapping
public class BaseController {

    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    /**
     * 首页
     * @return
     */
    @GetMapping(value = "/")
    public String homePage(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,  //关键词搜索
                               @RequestParam(value = "order", required = false, defaultValue = "latest") String order,  //最新或最热latest/popular
                               @RequestParam(value = "async", required = false) boolean async,  //是否异步请求
                               @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
                               Model model){
        Page<Blog> blogs = null;
        Pageable pageable;
        List<Blog> blogList = null;
        if(!keyword.equals("") && keyword != null) {  //keyword搜索

            if(order.equals("popular")){
                Sort sort = new Sort(Sort.Direction.DESC, "upVoteTimes", "readTimes", "commentTimes");
                pageable = PageRequest.of(pageIndex, pageSize, sort);
                blogs = blogService.getBlogsByKeyword(keyword, pageable);
                blogList = blogs.getContent();
            }else if(order.equals("latest")){
                Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
                pageable = PageRequest.of(pageIndex, pageSize, sort);
                blogs = blogService.getBlogsByKeyword(keyword, pageable);
                blogList = blogs.getContent();
            }
        }else if(order.equals("popular")){  //热度查询
            Sort sort = new Sort(Sort.Direction.DESC, "upVoteTimes", "readTimes", "commentTimes");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            blogs = blogService.getBlogsPopular(pageable);
            blogList = blogs.getContent();
        } else if(order.equals("latest")){   //默认情况下最新查询
            Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            blogs = blogService.getBlogsLatest(pageable);
            blogList = blogs.getContent();
        }

        User currentUser = null;
        boolean haveLogin = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            haveLogin = true;
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("haveLogin", haveLogin);
        model.addAttribute("blogList", blogList);
        model.addAttribute("keyword", keyword);
        return (async == true ? "/bases/home :: #blogListReplace" : "/bases/home");

    }

    /**
     * 注册页面
     * @return
     */
    @GetMapping(value = "/register")
    public String register(){
        return "bases/register";
    }

    @PostMapping(value = "/register")
    public String registerUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorityList(authorities);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //随机生成头像
        Random random = new Random();
        int num = random.nextInt(10) + 1;
        String avatarUrl = "/images/" + num + ".png";
        user.setAvatarurl(avatarUrl);
        userService.registerUser(user);
        return "redirect:/login";
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
