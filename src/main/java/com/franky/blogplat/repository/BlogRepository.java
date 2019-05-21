package com.franky.blogplat.repository;

import com.franky.blogplat.domain.Blog;
import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.Comment;
import com.franky.blogplat.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {
    /**
     * 目录查询/个人
     * @param classification
     * @return
     */
    List<Blog> findBlogsByClassification(Classification classification);

    /**
     * 热度新鲜度查询/个人
     * @param user
     * @param pageable
     * @return
     */
    Page<Blog> findBlogsByUser(User user, Pageable pageable);

    /**
     * 关键词查询/个人
     * @param user
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Blog> findBlogsByUserAndTitleLikeAndContentLike(User user, String title, String content, Pageable pageable);

    Page<Blog> findAll(Pageable pageable);

    Page<Blog> findBlogsByTitleLikeAndContentLike(String title, String content, Pageable pageable);
}
