package com.franky.blogplat.controller;

import com.franky.blogplat.VO.Response;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.service.BlogService;
import com.franky.blogplat.service.UpVoteService;
import com.franky.blogplat.utils.ConstraintViolationExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * Created by Franky on 2019/5/13.
 */
@Controller
@RequestMapping("/votes")
public class UpvoteController {

    @Autowired
    private UpVoteService upVoteService;

    @Autowired
    private BlogService blogService;
    /**
     * 提交点赞
     * @param blogId
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Response> submitUpVote(Long blogId){
        try {
            upVoteService.upVoteForBlog(blogId);
        }catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    @DeleteMapping("/{upVoteId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Response> cancelUpVote(Long blogId, @PathVariable("upVoteId") Long upVoteId){
        boolean isOwner = false;
        String userName = upVoteService.getUpVoteById(upVoteId).getUser().getUsername();
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && userName.equals(principal.getUsername()))
                isOwner = true;
        }

        if (!isOwner)
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));

        try {
            upVoteService.removeUpVote(blogId, upVoteId);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionUtil.getErrorInfo(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "已删除点赞", null));
    }

}
