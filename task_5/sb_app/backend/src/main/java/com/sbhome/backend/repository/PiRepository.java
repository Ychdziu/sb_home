package com.sbhome.backend.repository;

import java.math.BigDecimal;

public interface PiRepository {
    BigDecimal getPiValue(int precision);
}