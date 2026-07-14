package com.sbhome.backend.service;

import com.sbhome.backend.dto.PiValueResponse;

public interface PiService {
    PiValueResponse getPiValue(int precision);
}