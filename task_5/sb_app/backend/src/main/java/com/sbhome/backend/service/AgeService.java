package com.sbhome.backend.service;

import com.sbhome.backend.dto.AgeTextResponse;

public interface AgeService {
    AgeTextResponse getAgeText(int age);
}