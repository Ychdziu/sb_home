package com.sbhome.backend.repository;

import com.sbhome.backend.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceRepository {
    List<InvoiceResponse> getUnpaidInvoices();
}