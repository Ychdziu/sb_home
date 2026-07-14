package com.sbhome.backend.service;

import com.sbhome.backend.dto.AgeTextResponse;
import com.sbhome.backend.repository.AgeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgeServiceImplTest {

    @Mock
    AgeRepository ageRepository;

    @Test
    void returnsAgeAndTextFromRepository() {
        when(ageRepository.getAgeText(25)).thenReturn("You are adult");

        AgeServiceImpl service = new AgeServiceImpl(ageRepository);
        AgeTextResponse result = service.getAgeText(25);

        assertEquals(25, result.age());
        assertEquals("You are adult", result.text());
    }
}