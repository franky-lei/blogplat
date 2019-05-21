package com.franky.blogplat.repository;

import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by Franky on 2019/4/27.
 */
public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    List<Classification> findByUser(User user);
}
