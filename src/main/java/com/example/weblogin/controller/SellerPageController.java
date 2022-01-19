package com.example.weblogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerPageController {

    @GetMapping("/seller/{id}")
    public String sellerPage() {

        return "seller/sellerPage";
    }
}
