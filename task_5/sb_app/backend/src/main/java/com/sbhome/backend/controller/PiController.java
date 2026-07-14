package com.sbhome.backend.controller;

import com.sbhome.backend.dto.PiValueResponse;
import com.sbhome.backend.service.PiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PiController {

    private final PiService piService;

    public PiController(PiService piService) {
        this.piService = piService;
    }

    @GetMapping("/pi")
    public PiValueResponse getPiValue(@RequestParam int precision) {
        return piService.getPiValue(precision);
    }
}