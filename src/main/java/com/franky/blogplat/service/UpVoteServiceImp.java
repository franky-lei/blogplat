package com.franky.blogplat.service;

import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.UpVote;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.repository.BlogRepository;
import com.franky.blogplat.repository.UpVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by Franky on 2019/4/27.
 */
@Service
public class UpVoteServiceImp implements UpVoteService {

    @Autowired
    private UpVoteRepository upVoteRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogService blogService;

    /**
     * 给未点赞的博客点赞
     * @param blogId
     */
    @Override
    public void upVoteForBlog(Long blogId) {
        Blog blog = blogRepository.getOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UpVote upVote = new UpVote(user);
        if(!blog.addUpVote(upVote))
            throw new IllegalArgumentException("该用户已点过赞");
        blogRepository.save(blog);
    }

    /**
     * 取消点赞
     * @param blogId
     * @param upVoteId
     */
    @Override
    public void removeUpVote(Long blogId, Long upVoteId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeUpVote(upVoteId);
        upVoteRepository.deleteById(upVoteId);
        blogService.saveBlog(blog);
    }

    @Override
    public UpVote getUpVoteById(Long blogId) {
        return upVoteRepository.getOne(blogId);
    }
}
