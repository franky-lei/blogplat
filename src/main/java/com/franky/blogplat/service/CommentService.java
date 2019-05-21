package com.franky.blogplat.service;

import com.franky.blogplat.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
public interface CommentService {

    Comment getCommentById(Long commentId);

    void removeCommentById(Long commentId);

}
