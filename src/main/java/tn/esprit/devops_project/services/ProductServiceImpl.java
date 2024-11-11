package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.services.Iservices.IProductService;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    final ProductRepository productRepository;
    final StockRepository stockRepository;

    @Override
    public Product addProduct(Product product, Long idStock) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        // Retrieve the stock by ID or throw an exception if not found
        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new NullPointerException("Stock not found with ID: " + idStock));

        logger.info("Adding product to stock with ID: {}", idStock);

        // Input Validation
        if (product.getTitle() == null || product.getTitle().isEmpty()) {
            product.setTitle("Untitled Product");
            logger.warn("Product title is empty. Setting default title: 'Untitled Product'");
        }

        if (product.getPrice() == 0) {
            product.setPrice(10.0f); // Set default price if invalid
            logger.warn("Product price is invalid 😊 0). Setting default price: 10.0");
        }

        if (product.getQuantity() < 0) {
            product.setQuantity(0); // Set default quantity if invalid
            logger.warn("Product quantity is negative. Setting default quantity: 0");
        }

        // Auto-assign category based on the product title
        if (product.getCategory() == null) {
            if (product.getTitle().toLowerCase().contains("book")) {
                product.setCategory(ProductCategory.BOOKS);
                logger.info("Auto-assigning category to 'BOOKS' based on title content.");
            } else if (product.getTitle().toLowerCase().contains("electronics")) {
                product.setCategory(ProductCategory.ELECTRONICS);
                logger.info("Auto-assigning category to 'ELECTRONICS' based on title content.");
            } else {
                product.setCategory(ProductCategory.CLOTHING);
                logger.info("Auto-assigning category to 'OTHER' as no specific match found.");
            }
        }

        // Link product to stock and save it
        product.setStock(stock);
        Product savedProduct = productRepository.save(product);

        logger.info("Product added successfully with ID: {}", savedProduct.getIdProduct());
        return savedProduct;
    }

    @Override
    public Product retrieveProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NullPointerException("Product not found"));
    }

    @Override
    public List<Product> retreiveAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> retrieveProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> retreiveProductStock(Long id) {
        return productRepository.findByStockIdStock(id);
    }
}