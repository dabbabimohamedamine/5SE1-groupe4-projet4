package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.entities.SupplierDTO;
import tn.esprit.devops_project.repositories.SupplierRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAdvancedSupplier() {
        // Arrange
        SupplierDTO supplierToAdd = new SupplierDTO();
        supplierToAdd.setCode("uniqueCode");
        supplierToAdd.setLabel("Test Supplier Advanced");
        supplierToAdd.setSupplierCategory(SupplierCategory.ORDINAIRE);  // Using the enum directly

        Supplier supplierEntity = new Supplier();
        supplierEntity.setIdSupplier(1L);
        supplierEntity.setCode("uniqueCode");
        supplierEntity.setLabel("Test Supplier Advanced");
        supplierEntity.setSupplierCategory(SupplierCategory.ORDINAIRE);

        // Mocking the repository behavior
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplierEntity);

        // Act
        SupplierDTO savedSupplier = supplierService.addAdvancedSupplier(supplierToAdd);

        // Assert
        assertNotNull(savedSupplier);
        assertEquals("uniqueCode", savedSupplier.getCode());
        assertEquals("Test Supplier Advanced", savedSupplier.getLabel());
        assertEquals(SupplierCategory.ORDINAIRE, savedSupplier.getSupplierCategory());

        // Verify interactions with the repository
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }


}
