package com.example.weblogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/user/{id}")
    public String userPage() {

        return "user/userPage";
    }
}
