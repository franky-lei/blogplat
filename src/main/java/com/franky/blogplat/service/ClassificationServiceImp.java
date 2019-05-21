package com.franky.blogplat.service;

import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;
import com.franky.blogplat.repository.ClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
@Service
public class ClassificationServiceImp implements ClassificationService {

    @Autowired
    private ClassificationRepository classificationRepository;

    @Override
    public Classification getClassificationById(Long id) {
        return classificationRepository.getOne(id);
    }

    @Override
    public List<Classification> getClassificationsByUser(User user) {
        return classificationRepository.findByUser(user);
    }

    @Override
    public Classification saveClassification(Classification classification) {
        return classificationRepository.save(classification);
    }
}
