package com.example.thymeleafproject;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class TimeController {
    @GetMapping("/time")
    public String time(Model model){
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = currentTime.format(formatter);
            model.addAttribute("time", formattedTime);
            return "time";
    }
}
