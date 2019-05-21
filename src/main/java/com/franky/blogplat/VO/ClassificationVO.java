package com.franky.blogplat.VO;

import com.franky.blogplat.domain.Classification;

import java.io.Serializable;

/**
 * Created by Franky on 2019/5/18.
 */
public class ClassificationVO implements Serializable {
    private String username;

    private Classification classification;

    public ClassificationVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }
}
