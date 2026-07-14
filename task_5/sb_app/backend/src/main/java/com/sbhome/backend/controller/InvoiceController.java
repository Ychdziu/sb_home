package com.sbhome.backend.controller;

import com.sbhome.backend.dto.InvoiceResponse;
import com.sbhome.backend.service.InvoiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices/unpaid")
    public List<InvoiceResponse> getUnpaidInvoices() {
        return invoiceService.getUnpaidInvoices();
    }
}