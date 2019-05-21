package com.franky.blogplat.service;

import com.franky.blogplat.domain.Classification;
import com.franky.blogplat.domain.User;

import java.util.List;

/**
 * Created by Franky on 2019/4/27.
 */
public interface ClassificationService  {

    Classification getClassificationById(Long id);

    List<Classification> getClassificationsByUser(User user);

    Classification saveClassification(Classification classification);
}
