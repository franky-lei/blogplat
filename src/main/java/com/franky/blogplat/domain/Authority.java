package com.franky.blogplat.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by Franky on 2019/4/23.
 */
@Entity
public class Authority implements GrantedAuthority {

    private final static Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //权限唯一识别号

    @NotEmpty
    @Column(nullable = false)
    private String name;  //权限名（权限点）

    @Override
    public String getAuthority() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
