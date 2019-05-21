package com.franky.blogplat.service;

import com.franky.blogplat.domain.UpVote;

/**
 * Created by Franky on 2019/4/27.
 */
public interface UpVoteService {

    UpVote getUpVoteById(Long blogId);

    void upVoteForBlog(Long blogId);

    void removeUpVote(Long blogId, Long upVoteId);
}
