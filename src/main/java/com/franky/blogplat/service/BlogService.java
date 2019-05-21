package com.franky.blogplat.service;

import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
public interface BlogService {

    List<Blog> getUserBlogsByClassification(Classification classification);

    Page<Blog> getUserBlogsByPopular(User user, Pageable pageable);

    Page<Blog> getUserBlogsByCreateTime(User user, Pageable pageable);

    Page<Blog> getUserBlogsByKeyword(User user, String keyword, Pageable pageable);

    Page<Blog> getBlogsByKeyword(String keyword, Pageable pageable);

    Page<Blog> getBlogsPopular(Pageable pageable);

    Page<Blog> getBlogsLatest(Pageable pageable);

    Blog getBlogById(Long Id);

    Blog saveBlog(Blog blog);

    Blog addCommentToBlog(Long blogId, String commentContent);

    void removeCommentFromBlog(Long blogId, Long commentId);

    void removeUpVoteFromBlog(Long blogId, Long upVoteId);

    void increaseReadTimes(Long blogId);

    void removeBlog(Long blogId);
}
