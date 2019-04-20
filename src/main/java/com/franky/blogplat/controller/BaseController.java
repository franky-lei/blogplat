package com.franky.blogplat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Franky on 2019/4/20.
 */
@Controller
@RequestMapping
public class BaseController {

    @GetMapping(value = "/")
    public String bases(){
        return "bases/home";
    }

}
