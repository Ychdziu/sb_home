package com.sbhome.backend.controller;

import com.sbhome.backend.dto.AgeTextResponse;
import com.sbhome.backend.service.AgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AgeController {

    private final AgeService ageService;

    public AgeController(AgeService ageService) {
        this.ageService = ageService;
    }

    @GetMapping("/age-text")
    public AgeTextResponse getAgeText(@RequestParam int age) {
        return ageService.getAgeText(age);
    }
}