package com.sbhome.backend.service;

import com.sbhome.backend.dto.PiValueResponse;
import com.sbhome.backend.repository.PiRepository;
import org.springframework.stereotype.Service;

@Service
public class PiServiceImpl implements PiService {

    private final PiRepository piRepository;

    public PiServiceImpl(PiRepository piRepository) {
        this.piRepository = piRepository;
    }

    @Override
    public PiValueResponse getPiValue(int precision) {
        return new PiValueResponse(precision, piRepository.getPiValue(precision));
    }
}