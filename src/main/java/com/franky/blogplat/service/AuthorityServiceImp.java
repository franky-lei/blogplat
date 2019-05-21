package com.franky.blogplat.service;

import com.franky.blogplat.domain.Authority;
import com.franky.blogplat.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Franky on 2019/5/18.
 */
@Service
public class AuthorityServiceImp implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.getOne(id);
    }
}
