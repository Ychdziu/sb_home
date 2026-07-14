package com.sbhome.backend.service;

import com.sbhome.backend.dto.AgeTextResponse;
import com.sbhome.backend.repository.AgeRepository;
import org.springframework.stereotype.Service;

@Service
public class AgeServiceImpl implements AgeService {

    private final AgeRepository ageRepository;

    public AgeServiceImpl(AgeRepository ageRepository) {
        this.ageRepository = ageRepository;
    }

    @Override
    public AgeTextResponse getAgeText(int age) {
        String text = ageRepository.getAgeText(age);
        return new AgeTextResponse(age, text);
    }
}