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
    private InvoiceServiceImpl invoiceService; // Injecting the service to be tested

    @Mock
    private InvoiceRepository invoiceRepository; // Mocking the repository

    @BeforeEach
    public void setup() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);

        // Set up mock behavior for the repository
        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(
                new Date(2023 - 1900, 9, 1),
                new Date(2023 - 1900, 9, 6)))
                .thenReturn(300.0f);  // Mock the total amount result

        // You can set up additional mock behaviors if needed for other tests
    }

    @Test
    public void testGetTotalAmountInvoiceBetweenDates() {
        // Define the date range
        Date startDate = new Date(2023 - 1900, 9, 1); // Oct 1, 2023
        Date endDate = new Date(2023 - 1900, 9, 6); // Oct 6, 2023

        // Call the method and get the result
        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        // Verify that the total matches the expected amount (mocked as 300)
        assertEquals(300.0f, totalAmount);
    }
}
