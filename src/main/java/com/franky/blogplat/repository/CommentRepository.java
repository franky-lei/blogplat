package com.franky.blogplat.repository;

import com.franky.blogplat.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
