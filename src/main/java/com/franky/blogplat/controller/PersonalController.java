package com.franky.blogplat.controller;

import com.franky.blogplat.VO.ClassificationVO;
import com.franky.blogplat.VO.Response;
import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.service.BlogService;
import com.franky.blogplat.service.ClassificationService;
import com.franky.blogplat.service.UserService;
import com.franky.blogplat.utils.ConstraintViolationExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by Franky on 2019/4/25.
 */
@Controller
@RequestMapping("/p")
public class PersonalController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 获得用户页面，包括用户基本信息和用户博客列表
     * @param id
     * @param keyword
     * @param order
     * @param async
     * @param classificationId
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(value = "/{id}")
    public String personalPage(@PathVariable("id") Long id,
                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,  //关键词搜索
                               @RequestParam(value = "order", required = false, defaultValue = "latest") String order,  //最新或最热latest/popular
                               @RequestParam(value = "async", required = false) boolean async,  //是否异步请求
                               @RequestParam(value = "classification", required = false) Long classificationId,  //分类搜索
                               @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
                               Model model){
        User user = userService.getUserById(id);
        Page<Blog> blogs = null;
        Pageable pageable;
        List<Blog> blogList = null;
        if(classificationId != null){  //分类查询
            Classification classification = classificationService.getClassificationById(classificationId);
            blogList = blogService.getUserBlogsByClassification(classification);
        }else if(order.equals("popular")){  //热度查询
            Sort sort = new Sort(Sort.Direction.DESC, "upVoteTimes", "readTimes", "commentTimes");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            blogs = blogService.getUserBlogsByPopular(user, pageable);
            blogList = blogs.getContent();
        }else if(!keyword.equals("") && keyword != null){  //keyword搜索
            pageable = PageRequest.of(pageIndex, pageSize);
            blogs = blogService.getUserBlogsByKeyword(user, keyword, pageable);
            blogList = blogs.getContent();
        }else if(order.equals("latest")){   //默认情况下最新查询
            Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            blogs = blogService.getUserBlogsByCreateTime(user, pageable);
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
        model.addAttribute("user", user);
        return (async == true ? "/personal/person :: #blogListReplace" : "/personal/person");
    }

    /**
     * 获得分类部分页（识别分类的拥有者）
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/classification")
    public String listClassification(@RequestParam(value = "username", required = true) String username, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Classification> classificationList = classificationService.getClassificationsByUser(user);
        boolean isOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal.getUsername().equals(user.getUsername()))
                isOwner = true;
        }
        model.addAttribute("isOwnClassification", isOwner);
        model.addAttribute("classifications", classificationList);
        return "/personal/person :: #classificationsReplace";
    }

    /**
     * 获取分类编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/classification/edit")
    public String getCatalogEdit(Model model) {
        Classification newClassification = new Classification(null, null);
        model.addAttribute("newClassification", newClassification);
        return "/personal/person :: #classificationFormReplace";
    }

    /**
     * 添加文章分类
     * @param classificationVO
     * @return
     */
    @PostMapping("/classification")
    @PreAuthorize("authentication.name.equals(#classificationVO.username)")
    public ResponseEntity<Response> postClassification(@RequestBody ClassificationVO classificationVO){
        Classification classification = classificationVO.getClassification();
        String username = classificationVO.getUsername();
        User user = (User) userDetailsService.loadUserByUsername(username);
        try{
            classification.setUser(user);
            classificationService.saveClassification(classification);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "创建分类成功"));
    }
}
