package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Stock stock;
    private Product product;

    @BeforeEach
    void setUp() {

        stock = new Stock();
        stock.setIdStock(1L);
        stock.setTitle("Main Stock");
        stock.setSensitive(true);

        product = new Product();
        product.setIdProduct(1L);
        product.setTitle("Laptop");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setPrice(1500);
        product.setQuantity(5);
    }

    @Test
    void
    testAddProduct_Success() {

        when(stockRepository.findById(1L)).thenReturn(java.util.Optional.of(stock));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdProduct());
        assertEquals("Laptop", result.getTitle());
        assertEquals(ProductCategory.ELECTRONICS, result.getCategory());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testAddProduct_PriceValidationFail() {

        product.setPrice(-100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product, 1L);
        });
        assertEquals("Product price must be greater than zero", exception.getMessage());
    }

    @Test
    void testAddProduct_QuantityValidationFail() {
        product.setQuantity(-5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product, 1L);
        });
        assertEquals("Product quantity cannot be negative", exception.getMessage());
    }


    @Test
    void testAddProduct_LowQuantityWarning() {

        product.setQuantity(5);

        when(stockRepository.findById(1L)).thenReturn(java.util.Optional.of(stock));
        when(productRepository.save(product)).thenReturn(product);

        productService.addProduct(product, 1L);
        verify(productRepository, times(1)).save(product);
    }
}
