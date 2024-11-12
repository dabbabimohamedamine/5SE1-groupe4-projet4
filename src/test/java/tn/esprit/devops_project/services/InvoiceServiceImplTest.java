package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.repositories.InvoiceRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(
                new Date(2023 - 1900, 9, 1),
                new Date(2023 - 1900, 9, 6)))
                .thenReturn(300.0f);

    }

    @Test
    public void testGetTotalAmountInvoiceBetweenDates() {
        Date startDate = new Date(2023 - 1900, 9, 1); // Oct 1, 2023
        Date endDate = new Date(2023 - 1900, 9, 6); // Oct 6, 2023

        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        assertEquals(300.0f, totalAmount);
    }
}
