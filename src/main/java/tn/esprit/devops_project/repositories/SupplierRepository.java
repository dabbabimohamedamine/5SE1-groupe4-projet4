package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devops_project.entities.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByCode(String code);
    Supplier findByCode(String code);
}
