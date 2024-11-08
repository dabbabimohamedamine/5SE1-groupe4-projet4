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

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

   final ProductRepository productRepository;
   final StockRepository stockRepository;


    @Override
    public Product addProduct(Product product, Long idStock) {
        log.info("Attempting to add a new product: {}", product);

        if (product.getPrice() <= 0) {
            log.error("Product price must be greater than zero. Provided price: {}", product.getPrice());
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (product.getQuantity() < 0) {
            log.error("Product quantity cannot be negative. Provided quantity: {}", product.getQuantity());
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }

        List<Product> existingProducts = productRepository.findByTitleAndCategoryAndStockIdStock(product.getTitle(), product.getCategory(), idStock);
        if (!existingProducts.isEmpty()) {
            log.warn("A similar product already exists in stock. Duplicate not allowed for title: {} in category: {}", product.getTitle(), product.getCategory());
            throw new IllegalArgumentException("A product with the same title and category already exists in this stock");
        }

        Stock stock = stockRepository.findById(idStock)
                .orElseThrow(() -> new NullPointerException("Stock not found with ID: " + idStock));
        product.setStock(stock);

        if (product.getCategory() == ProductCategory.RESTRICTED && stock.isSensitive()) {
            log.error("Cannot add restricted products to sensitive stock locations");
            throw new IllegalArgumentException("Restricted products cannot be added to sensitive stock locations");
        }

        if (product.getQuantity() < 10) {
            log.warn("Product quantity is low ({} units). Consider ordering more stock.", product.getQuantity());
        }

        Product savedProduct = productRepository.save(product);
        log.info("Successfully added product with ID: {} to stock with ID: {}", savedProduct.getIdProduct(), idStock);

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
