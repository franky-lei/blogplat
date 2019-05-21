package com.franky.blogplat.service;

import com.franky.blogplat.domain.User;

/**
 * Created by Franky on 2019/4/23.
 */
public interface UserService {

    User getUserById(Long id);

    User registerUser(User user);
}
