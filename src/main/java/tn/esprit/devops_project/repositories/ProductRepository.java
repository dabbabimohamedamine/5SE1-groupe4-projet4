package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByStockIdStock(Long idStock);

    // Method to find products by title, category, and stock ID for duplicate checks
    List<Product> findByTitleAndCategoryAndStockIdStock(String title, ProductCategory category, Long stockId);
}
