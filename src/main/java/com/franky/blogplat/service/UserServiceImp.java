package com.franky.blogplat.service;

import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.repository.ClassificationRepository;
import com.franky.blogplat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Franky on 2019/4/23.
 */
@Service
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassificationRepository classificationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        User savedUser = userRepository.save(user);
        Classification classification = new Classification("默认目录", savedUser);
        classificationRepository.save(classification);
        return savedUser;
    }
}
