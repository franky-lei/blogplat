package com.franky.blogplat.controller;

import com.franky.blogplat.VO.Response;
import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Comment;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.service.BlogService;
import com.franky.blogplat.service.CommentService;
import com.franky.blogplat.utils.ConstraintViolationExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by Franky on 2019/5/13.
 */
@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    /**
     * 获取评论框及评论列表
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping
    public String commentList(@RequestParam(value = "blogId", required = true)Long blogId, Model model){
        Long commentOwnId = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(null != currentUser)
                commentOwnId = currentUser.getId();
        }
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> commentList = blog.getComments();
        model.addAttribute("commentOwnId", commentOwnId);
        model.addAttribute("commentList", commentList);
        return "/personal/blog :: #commentAreaReplace";
    }

    /**
     * 提交评论内容
     * @param blogId
     * @param commentContent
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE')")
    public ResponseEntity<Response> postComment(@RequestParam(value = "blogId", required = true) Long blogId,
                                                @RequestParam(value = "commentContent", required = true) String commentContent){

        try{
            blogService.addCommentToBlog(blogId, commentContent);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "评论提交成功"));
    }

    /**
     * 删除评论（需要评论拥有者权限）
     * @param commentId
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE')")
    public ResponseEntity<Response> deleteComment(@PathVariable(value = "id") Long commentId, @RequestParam(value = "blogId", required = true) Long blogId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean ownComment = false;
        if(commentService.getCommentById(commentId).getUser().getUsername().equals(currentUser.getUsername()))
            ownComment = true;

        if(!ownComment)
            return ResponseEntity.ok().body(new Response(false, "无删评权限"));

        try{
            blogService.removeCommentFromBlog(blogId, commentId);
            commentService.removeCommentById(commentId);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "评论删除成功"));
    }

}
