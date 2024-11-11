package tn.esprit.devops_project.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.Optional;

class ProductServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Test
    void addProduct_shouldSetDefaultValuesAndAssignCategory_whenInvalidDataIsProvided() {
        // Arrange
        Long stockId = 1L;
        Stock mockStock = new Stock();
        mockStock.setIdStock(stockId);

        Product product = new Product();
        product.setTitle("");
        product.setPrice(-5);
        product.setQuantity(-10);
        product.setCategory(null);

        Product savedProduct = new Product();
        savedProduct.setIdProduct(1L);
        savedProduct.setTitle("Untitled Product");
        savedProduct.setPrice(10.0f);
        savedProduct.setQuantity(0);
        savedProduct.setCategory(ProductCategory.CLOTHING);

        // Mocking dependencies
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(mockStock));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.addProduct(product, stockId);

        // Assert
        assertNotNull(result);
        assertEquals("Untitled Product", result.getTitle());
        assertEquals(10.0f, result.getPrice());
        assertEquals(0, result.getQuantity());
        assertEquals(ProductCategory.CLOTHING, result.getCategory());

        // Log the product details
        logger.info("Product added: {}", result);

        // Verify interactions
        verify(stockRepository, times(1)).findById(stockId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_shouldAssignCategoryAsBooks_whenTitleContainsBook() {
        // Arrange
        Long stockId = 1L;
        Stock mockStock = new Stock();
        mockStock.setIdStock(stockId);

        Product product = new Product();
        product.setTitle("Programming Book");
        product.setPrice(50.0f);
        product.setQuantity(5);
        product.setCategory(null);

        Product savedProduct = new Product();
        savedProduct.setIdProduct(1L);
        savedProduct.setTitle("Programming Book");
        savedProduct.setPrice(50.0f);
        savedProduct.setQuantity(5);
        savedProduct.setCategory(ProductCategory.BOOKS);

        // Mocking dependencies
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(mockStock));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.addProduct(product, stockId);

        // Assert
        assertNotNull(result);
        assertEquals("Programming Book", result.getTitle());
        assertEquals(50.0f, result.getPrice());
        assertEquals(5, result.getQuantity());
        assertEquals(ProductCategory.BOOKS, result.getCategory());

        // Log the product details
        logger.info("Product added: {}", result);

        // Verify interactions
        verify(stockRepository, times(1)).findById(stockId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_shouldSetDefaultPrice_whenProductHasInvalidPrice() {
        // Arrange
        Long stockId = 1L;
        Stock mockStock = new Stock();
        mockStock.setIdStock(stockId);

        // Creating a product with an invalid price (negative price)
        Product product = new Product();
        product.setTitle("Valid Product");
        product.setPrice(-10.0f); // Invalid price
        product.setQuantity(5);
        product.setCategory(ProductCategory.CLOTHING);

        Product savedProduct = new Product();
        savedProduct.setIdProduct(1L);
        savedProduct.setTitle("Valid Product");
        savedProduct.setPrice(10.0f); // Default price set
        savedProduct.setQuantity(5);
        savedProduct.setCategory(ProductCategory.CLOTHING);

        // Mocking dependencies
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(mockStock));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.addProduct(product, stockId);

        // Assert
        assertNotNull(result);
        assertEquals("Valid Product", result.getTitle());
        assertEquals(10.0f, result.getPrice()); // Assert that default price is set
        assertEquals(5, result.getQuantity());
        assertEquals(ProductCategory.CLOTHING, result.getCategory());

        // Log the product details
        logger.info("Product added: {}", result);

        // Verify interactions
        verify(stockRepository, times(1)).findById(stockId);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}