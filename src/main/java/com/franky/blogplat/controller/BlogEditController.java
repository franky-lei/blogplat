package com.franky.blogplat.controller;

import com.franky.blogplat.VO.Response;
import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.service.BlogService;
import com.franky.blogplat.service.ClassificationService;
import com.franky.blogplat.utils.ConstraintViolationExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by Franky on 2019/4/30.
 */
@Controller
@RequestMapping("/writer")
public class BlogEditController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ClassificationService classificationService;

    /**
     * 获得首次编辑页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping(value = "/{username}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView editPage(@PathVariable("username") String username, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Classification> classificationList = classificationService.getClassificationsByUser(user);
        model.addAttribute("classificationList", classificationList);
        model.addAttribute("blog", new Blog(null, null, null));
        return new ModelAndView( "/personal/writer", "blogModel", model);
    }


    /**
     * 获得二次编辑页面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/{username}/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView changePage(@PathVariable("username") String username, @PathVariable("id") Long id, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Classification> classificationList = classificationService.getClassificationsByUser(user);
        model.addAttribute("classificationList", classificationList);
        model.addAttribute("blog", blogService.getBlogById(id));
        return new ModelAndView( "/personal/writer", "blogModel", model);
    }

    /**
     * 保存或更新文章
     * @param username
     * @param blog
     * @return
     */
    @PostMapping(value = "/{username}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> savePage(@PathVariable("username") String username, @RequestBody Blog blog){
        Blog savedBlog;
        try{
            if(null == blog.getId()){ //第一次编辑
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                savedBlog = blogService.saveBlog(blog);
            }else{  //非首次编辑
                Blog existedBlog = blogService.getBlogById(blog.getId());
                existedBlog.setTitle(blog.getTitle());
                existedBlog.setSummary(blog.getSummary());
                existedBlog.setContent(blog.getContent());
                existedBlog.setParsedContent(blog.getParsedContent());
                existedBlog.setTags(blog.getTags());
                existedBlog.setClassification(blog.getClassification());
                savedBlog = blogService.saveBlog(existedBlog);
            }
        }catch(ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/blog/" + savedBlog.getId();
        return ResponseEntity.ok().body(new Response(true, "提交成功", redirectUrl));
    }
}
