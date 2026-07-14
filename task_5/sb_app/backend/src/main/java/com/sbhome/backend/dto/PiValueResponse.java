package com.sbhome.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record PiValueResponse(
        int precision,
        @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal value
) {
}