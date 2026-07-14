package com.sbhome.backend.service;

import com.sbhome.backend.dto.InvoiceResponse;
import com.sbhome.backend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceResponse> getUnpaidInvoices() {
        return invoiceRepository.getUnpaidInvoices();
    }
}