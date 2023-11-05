package com.example.thymeleafproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutMeController {
    @GetMapping("/pawelstaciwa")
    public String aboutMe(){
        return "about_me";
    }
}
