package com.sbhome.backend.service;

import com.sbhome.backend.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    List<InvoiceResponse> getUnpaidInvoices();
}