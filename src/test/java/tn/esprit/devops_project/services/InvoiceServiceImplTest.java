package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.devops_project.repositories.InvoiceRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(
                LocalDate.of(2023, 10, 1),  // Oct 1, 2023
                LocalDate.of(2023, 10, 6))) // Oct 6, 2023
                .thenReturn(300.0f);
    }

    @Test
    void testGetTotalAmountInvoiceBetweenDates() {
        LocalDate startDate = LocalDate.of(2023, 10, 1); // Oct 1, 2023
        LocalDate endDate = LocalDate.of(2023, 10, 6);   // Oct 6, 2023

        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        assertEquals(300.0f, totalAmount);
    }
}
