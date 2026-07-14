package com.sbhome.backend.dto;

import java.time.LocalDate;

public record InvoiceResponse(Long invoiceId, LocalDate invoiceDate) {
}