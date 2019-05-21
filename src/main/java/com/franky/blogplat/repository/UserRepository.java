package com.franky.blogplat.repository;

import com.franky.blogplat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Franky on 2019/4/23.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

}
