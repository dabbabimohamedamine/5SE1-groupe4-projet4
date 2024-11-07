package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.SupplierDTO;
import tn.esprit.devops_project.services.Iservices.ISupplierService;
import java.util.List;

@RestController
@AllArgsConstructor
public class SupplierController {

	ISupplierService supplierService;

	@GetMapping("/supplier")
	public List<SupplierDTO> getSuppliers() {
		return supplierService.retrieveAllSuppliers();
	}

	@GetMapping("/supplier/{supplierId}")
	public SupplierDTO retrieveSupplier(@PathVariable Long supplierId) {
		return supplierService.retrieveSupplier(supplierId);
	}

	@PostMapping("/supplier")
	public SupplierDTO addSupplier(@RequestBody SupplierDTO supplierDTO) {
		return supplierService.addSupplier(supplierDTO);
	}

	@DeleteMapping("/supplier/{supplierId}")
	public void removeSupplier(@PathVariable Long supplierId) {
		supplierService.deleteSupplier(supplierId);
	}

	@PutMapping("/supplier")
	public SupplierDTO modifySupplier(@RequestBody SupplierDTO supplierDTO) {
		return supplierService.updateSupplier(supplierDTO);
	}
}
