package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Invoice;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IInvoiceService {
	List<Invoice> retrieveAllInvoices();

	Invoice addInvoice(Invoice invoice);

	List<Invoice> getInvoicesBySupplier(Long idSupplier);

	void cancelInvoice(Long id);

	Invoice retrieveInvoice(Long id);
	
	void assignOperatorToInvoice(Long idOperator, Long idInvoice);

	float getTotalAmountInvoiceBetweenDates(LocalDate startDate, LocalDate endDate);

}
