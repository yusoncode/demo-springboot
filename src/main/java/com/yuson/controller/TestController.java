package com.yuson.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author yuson
 * @date 2020-12-21
 */
@RequestMapping("test")
@RestController
public class TestController {

    @Value("${name}")
    private String name;

    @GetMapping("/name")
    public String rest(){
        return name;
    }
}
