package com.example.demowithecomjewel.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://customised-jewels.netlify.app") // your frontend URL
public class RootController {

    @GetMapping("/")
    public String home() {
        return "Backend is running!";
    }
}
