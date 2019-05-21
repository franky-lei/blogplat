package com.franky.blogplat.service;

import com.franky.blogplat.domain.Comment;
import com.franky.blogplat.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
@Service
public class CommentServiceImp implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.getOne(commentId);
    }

    @Override
    public void removeCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }


}
