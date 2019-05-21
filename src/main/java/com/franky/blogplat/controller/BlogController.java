package com.franky.blogplat.controller;

import com.franky.blogplat.VO.Response;
import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.UpVote;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Franky on 2019/5/9.
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 博客展示页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{id}")
    public String blogshow(@PathVariable("id") Long id, Model model){
        boolean ownBlog = false;
        boolean ownAdmin = false;
        boolean haveLogin = false;
        Blog blog = blogService.getBlogById(id);
        User currentUser = null;
        UpVote currentVote = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            haveLogin = true;
            if (currentUser != null && blog.getUser().getUsername().equals(currentUser.getUsername())) {
                ownBlog = true;  //判断访问者是否为博客的拥有者
                if(currentUser.getUsername().equals("admin"))
                    ownAdmin = true;
            }
        }

        if (currentUser != null){
            for(UpVote upVote : blog.getUpVotes()){
                if(upVote.getUser().getUsername().equals(currentUser.getUsername()))
                    currentVote = upVote;
            }
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("haveLogin", haveLogin);

        blogService.increaseReadTimes(id);
        model.addAttribute("currentVote", currentVote);
        model.addAttribute("blog", blog);
        model.addAttribute("ownBlog", ownBlog);
        model.addAttribute("ownAdmin", ownAdmin);
        return "/personal/blog";
    }


    @DeleteMapping("/{username}/{id}")
    @PreAuthorize("authentication.name.equals(#username) || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> blogdelete(@PathVariable("id") Long id, @PathVariable("username") String username){
        try{
            blogService.removeBlog(id);
        }catch(Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        User user = (User)userDetailsService.loadUserByUsername(username);
        Long userId = user.getId();
        String redirectUrl = "/p/" + userId;
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }
}
