package com.franky.blogplat.repository;

import com.franky.blogplat.domain.UpVote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franky on 2019/4/27.
 */
public interface UpVoteRepository extends JpaRepository<UpVote, Long> {
}
