package com.example.thymeleafproject;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class helloController {
@GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
