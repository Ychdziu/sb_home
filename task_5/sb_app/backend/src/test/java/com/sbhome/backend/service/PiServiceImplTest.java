package com.sbhome.backend.service;

import com.sbhome.backend.dto.PiValueResponse;
import com.sbhome.backend.repository.PiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PiServiceImplTest {

    @Mock
    PiRepository piRepository;

    @Test
    void returnsPrecisionAndValueFromRepository() {
        BigDecimal expected = new BigDecimal("3.14159265358979323846");
        when(piRepository.getPiValue(20)).thenReturn(expected);

        PiServiceImpl service = new PiServiceImpl(piRepository);
        PiValueResponse result = service.getPiValue(20);

        assertEquals(20, result.precision());
        assertEquals(expected, result.value());
    }
}