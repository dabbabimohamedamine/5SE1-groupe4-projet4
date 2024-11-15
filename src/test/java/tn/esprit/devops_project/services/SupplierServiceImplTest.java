package tn.esprit.devops_project.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierDTO;
import tn.esprit.devops_project.repositories.SupplierRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class SupplierServiceImplTest {

    @Autowired
    private SupplierServiceImpl supplierService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void testAddAdvancedSupplier() {
        SupplierDTO supplierToAdd = new SupplierDTO();
        supplierToAdd.setCode("uniqueCode");
        supplierToAdd.setLabel("Test Supplier Advanced");
        supplierToAdd.setSupplierCategory("ORDINAIRE");

        SupplierDTO savedSupplier = supplierService.addAdvancedSupplier(supplierToAdd);

        assertNotNull(savedSupplier);
        assertNotNull(savedSupplier.getIdSupplier());
        assertEquals("uniqueCode", savedSupplier.getCode());

        Supplier foundSupplier = supplierRepository.findById(savedSupplier.getIdSupplier()).orElse(null);
        assertNotNull(foundSupplier);
        assertEquals(savedSupplier.getCode(), foundSupplier.getCode());
        assertEquals(savedSupplier.getLabel(), foundSupplier.getLabel());

        supplierRepository.deleteById(savedSupplier.getIdSupplier());
    }


}
