package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.entities.SupplierDTO;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

	private final SupplierRepository supplierRepository;

	@Override
	public List<SupplierDTO> retrieveAllSuppliers() {
		return supplierRepository.findAll().stream()
				.map(this::mapToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public SupplierDTO addSupplier(SupplierDTO supplierDTO) {
		Supplier supplier = mapToEntity(supplierDTO);
		return mapToDTO(supplierRepository.save(supplier));
	}

	@Override
	public SupplierDTO updateSupplier(SupplierDTO supplierDTO) {
		Supplier supplier = mapToEntity(supplierDTO);
		return mapToDTO(supplierRepository.save(supplier));
	}

	@Override
	public void deleteSupplier(Long supplierId) {
		supplierRepository.deleteById(supplierId);
	}

	@Override
	public SupplierDTO retrieveSupplier(Long supplierId) {
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(() -> new IllegalArgumentException("Supplier with ID " + supplierId + " not found."));
		return mapToDTO(supplier);
	}

	// Mapping Methods
	private SupplierDTO mapToDTO(Supplier supplier) {
		if (supplier == null) {
			return null; // Return null if the entity is null
		}
		SupplierDTO supplierDTO = new SupplierDTO();
		supplierDTO.setCode(supplier.getCode());
		supplierDTO.setLabel(supplier.getLabel());
		supplierDTO.setSupplierCategory(supplier.getSupplierCategory());
		return supplierDTO;
	}


	private Supplier mapToEntity(SupplierDTO supplierDTO) {
		if (supplierDTO == null) {
			return null; // Return null if the DTO is null
		}
		Supplier supplier = new Supplier();
		supplier.setCode(supplierDTO.getCode());
		supplier.setLabel(supplierDTO.getLabel());
		supplier.setSupplierCategory(SupplierCategory.valueOf(String.valueOf(supplierDTO.getSupplierCategory()))); // Ensure SupplierCategory is valid
		return supplier;
	}

	@Override
	public SupplierDTO addAdvancedSupplier(SupplierDTO supplierDTO) {
		// Ensure that the supplier code is not null or empty
		if (supplierDTO.getCode() == null || supplierDTO.getCode().isEmpty()) {
			throw new IllegalArgumentException("Le code du fournisseur ne peut pas être vide");
		}

		// Check if a supplier with the same code already exists
		if (supplierRepository.existsByCode(supplierDTO.getCode())) {
			throw new IllegalArgumentException("Le code du fournisseur doit être unique");
		}

		// Convert DTO to Entity
		Supplier supplier = mapToEntity(supplierDTO);

		// Null check for supplier entity after conversion
		if (supplier == null) {
			throw new IllegalArgumentException("Supplier entity could not be created from DTO.");
		}

		// Save the supplier and handle any possible issues with saving
		Supplier savedSupplier = supplierRepository.save(supplier);

		// Null check after saving the supplier to the repository
		if (savedSupplier == null) {
			throw new IllegalStateException("Failed to save supplier to the repository.");
		}

		// Convert the saved entity back to DTO and return it
		return mapToDTO(savedSupplier);
	}

}
