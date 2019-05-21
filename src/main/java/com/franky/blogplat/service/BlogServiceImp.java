package com.franky.blogplat.service;

import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.Comment;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
@Service
public class BlogServiceImp implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 目录查询
     * @param classification
     * @return
     */
    @Override
    public List<Blog> getUserBlogsByClassification(Classification classification) {
        return blogRepository.findBlogsByClassification(classification);
    }

    /**
     * 最热查询
     * @param user
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> getUserBlogsByPopular(User user, Pageable pageable) {
        return blogRepository.findBlogsByUser(user, pageable);
    }

    /**
     * 最新查询
     * @param user
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> getUserBlogsByCreateTime(User user, Pageable pageable) {
        return blogRepository.findBlogsByUser(user, pageable);
    }

    /**
     * keyword查询
     * @param user
     * @param keyword
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> getUserBlogsByKeyword(User user, String keyword, Pageable pageable) {
        String key = "%" + keyword + "%";
        return blogRepository.findBlogsByUserAndTitleLikeAndContentLike(user, key, key, pageable);
    }

    @Override
    public Blog getBlogById(Long Id) {
        return blogRepository.getOne(Id);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog addCommentToBlog(Long blogId, String commentContent) {
        Blog blog = blogRepository.getOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(commentContent, user);
        blog.addComments(comment);
        return this.saveBlog(blog);
    }

    @Override
    public void removeCommentFromBlog(Long blogId, Long commentId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeComment(commentId);
        this.saveBlog(blog);
    }

    @Override
    public void removeUpVoteFromBlog(Long blogId, Long upVoteId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeUpVote(upVoteId);
    }

    @Override
    public void increaseReadTimes(Long blogId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.setReadTimes(blog.getReadTimes() + 1);
    }

    @Override
    public Page<Blog> getBlogsByKeyword(String keyword, Pageable pageable) {
        String key = "%" + keyword + "%";
        return blogRepository.findBlogsByTitleLikeAndContentLike(key, key, pageable);
    }

    @Override
    public Page<Blog> getBlogsPopular(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> getBlogsLatest(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public void removeBlog(Long blogId) {
        blogRepository.deleteById(blogId);
    }
}
