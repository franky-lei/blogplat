package com.franky.blogplat.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Franky on 2019/4/23.
 */
@Entity
public class User implements UserDetails {

    private final static Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   //id是唯一的

    @NotEmpty(message = "必须填写账户")
    @Size(min = 3, max = 30)
    @Column(nullable = false, unique = true, length = 30)   //用户名必须是唯一的
    private String username;

    @NotEmpty(message = "必须填写邮箱")
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotEmpty(message = "必须填写昵称")
    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String name;

    @NotEmpty(message = "必须填写密码")
    @Size(max = 100)
    @Column(nullable = false, length = 100)   //用户名必须是唯一的
    private String password;

    @Column(length = 200)   //用户名必须是唯一的
    private String avatarurl;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorityList;

    protected User() {

    }

    public User(Long id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for(Authority authority : authorityList){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleGrantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
