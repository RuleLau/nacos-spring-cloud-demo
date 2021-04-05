package com.rule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public String redirect(HttpServletRequest request) {
        String s = request.getRequestURL().toString();
        return s;
    }

    @GetMapping("/testLogin")
    public String testLogin() {
        return "redirect:/login.html";
    }

    @PostMapping("/success")
    public String success() {
        return "redirect:/success.html";
    }

    @PostMapping("/fail")
    public String fail() {
        return "redirect:/fail.html";
    }
}
