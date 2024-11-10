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

	SupplierRepository supplierRepository;

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
				.orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id:" + supplierId));
		return mapToDTO(supplier);
	}

	// Mapping Methods
	private SupplierDTO mapToDTO(Supplier supplier) {
		return new SupplierDTO(
				supplier.getIdSupplier(),
				supplier.getCode(),
				supplier.getLabel(),
				supplier.getSupplierCategory().toString()
		);
	}

	private Supplier mapToEntity(SupplierDTO dto) {
		Supplier supplier = new Supplier();
		supplier.setIdSupplier(dto.getIdSupplier());
		supplier.setCode(dto.getCode());
		supplier.setLabel(dto.getLabel());
		supplier.setSupplierCategory(SupplierCategory.valueOf(dto.getSupplierCategory()));
		return supplier;
	}

	@Override
	public SupplierDTO addAdvancedSupplier(SupplierDTO supplierDTO) {
		if (supplierDTO.getCode() == null || supplierDTO.getCode().isEmpty()) {
			throw new IllegalArgumentException("Le code du fournisseur ne peut pas être vide");
		}

		if (supplierRepository.existsByCode(supplierDTO.getCode())) {
			throw new IllegalArgumentException("Le code du fournisseur doit être unique");
		}

		Supplier supplier = mapToEntity(supplierDTO);
		return mapToDTO(supplierRepository.save(supplier));
	}

}
