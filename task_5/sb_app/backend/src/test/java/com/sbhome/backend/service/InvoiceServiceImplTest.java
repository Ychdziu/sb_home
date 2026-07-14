package com.sbhome.backend.service;

import com.sbhome.backend.dto.InvoiceResponse;
import com.sbhome.backend.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    InvoiceRepository invoiceRepository;

    @Test
    void returnsUnpaidInvoicesFromRepository() {
        List<InvoiceResponse> expected = List.of(
                new InvoiceResponse(2L, LocalDate.of(2026, 7, 2)),
                new InvoiceResponse(6L, LocalDate.of(2026, 7, 6))
        );
        when(invoiceRepository.getUnpaidInvoices()).thenReturn(expected);

        InvoiceServiceImpl service = new InvoiceServiceImpl(invoiceRepository);
        List<InvoiceResponse> result = service.getUnpaidInvoices();

        assertEquals(expected, result);
    }
}