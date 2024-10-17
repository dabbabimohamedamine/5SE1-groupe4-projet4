package tn.esprit.devops_project.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.SupplierRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class SupplierServiceImplTest {

    @Autowired
    private SupplierServiceImpl supplierService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    public void testAddSupplier() {
        Supplier supplierToAdd = new Supplier();
        supplierToAdd.setCode("test");
        supplierToAdd.setLabel("Test Supplier");
        supplierToAdd.setSupplierCategory(SupplierCategory.ORDINAIRE);

        Supplier savedSupplier = supplierService.addSupplier(supplierToAdd);

        assertNotNull(savedSupplier);
        assertNotNull(savedSupplier.getIdSupplier());
        assertEquals("test", savedSupplier.getCode());

        Supplier foundSupplier = supplierRepository.findById(savedSupplier.getIdSupplier()).orElse(null);
        assertNotNull(foundSupplier);
        assertEquals(savedSupplier.getCode(), foundSupplier.getCode());
        assertEquals(savedSupplier.getLabel(), foundSupplier.getLabel());
    }
}
