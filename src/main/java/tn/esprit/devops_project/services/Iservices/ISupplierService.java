package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierDTO;

import java.util.List;

public interface ISupplierService {

	List<SupplierDTO> retrieveAllSuppliers();
	SupplierDTO addSupplier(SupplierDTO supplierDTO);
	SupplierDTO updateSupplier(SupplierDTO supplierDTO);
	void deleteSupplier(Long supplierId);
	SupplierDTO retrieveSupplier(Long supplierId);

	SupplierDTO addAdvancedSupplier(SupplierDTO supplierDTO);
}
