package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class StockServiceImplTest {

    @Autowired
    private StockRepository stockRepository;

    private StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockServiceImpl(stockRepository);

        // Ajouter des stocks à la base de données H2
        stockRepository.save(new Stock(1, "Stock A", 100.0, "Category1", null));
        stockRepository.save(new Stock(2, "Stock B", 150.0, "Category2", null));
        stockRepository.save(new Stock(3, "Stock C", 200.0, "Category1", null));
        stockRepository.save(new Stock(4, "Stock D", 120.0, "Category2", null));
    }

    @Test
    void testSearchAndSortStockAscendingByPrice() {
        String title = "Stock";
        String category = "Category1";
        Double minPrice = 100.0;
        Double maxPrice = 200.0;
        String sortBy = "price";
        boolean ascending = true;

        List<Stock> result = stockService.searchAndSortStock(title, category, minPrice, maxPrice, sortBy, ascending);

        assertEquals(2, result.size(), "Le nombre de résultats devrait être 2");
        assertEquals("Stock A", result.get(0).getTitle(), "Le premier stock devrait être 'Stock A'");
        assertEquals("Stock C", result.get(1).getTitle(), "Le deuxième stock devrait être 'Stock C'");
        assertTrue(result.get(0).getPrice() <= result.get(1).getPrice(), "Les stocks doivent être triés par prix croissant");
    }

    @Test
    void testSearchAndSortStockDescendingByPrice() {
        String title = "Stock";
        String category = "Category1";
        Double minPrice = 100.0;
        Double maxPrice = 200.0;
        String sortBy = "price";
        boolean ascending = false;

        List<Stock> result = stockService.searchAndSortStock(title, category, minPrice, maxPrice, sortBy, ascending);

        assertEquals(2, result.size(), "Le nombre de résultats devrait être 2");
        assertEquals("Stock C", result.get(0).getTitle(), "Le premier stock devrait être 'Stock C'");
        assertEquals("Stock A", result.get(1).getTitle(), "Le deuxième stock devrait être 'Stock A'");
        assertTrue(result.get(0).getPrice() >= result.get(1).getPrice(), "Les stocks doivent être triés par prix décroissant");
    }

    @Test
    void testSearchAndSortStockByCategoryOnly() {
        String title = null;
        String category = "Category2";
        Double minPrice = null;
        Double maxPrice = null;
        String sortBy = "title";
        boolean ascending = true;

        List<Stock> result = stockService.searchAndSortStock(title, category, minPrice, maxPrice, sortBy, ascending);

        assertEquals(2, result.size(), "Le nombre de résultats devrait être 2");
        assertEquals("Stock B", result.get(0).getTitle(), "Le premier stock devrait être 'Stock B'");
        assertEquals("Stock D", result.get(1).getTitle(), "Le deuxième stock devrait être 'Stock D'");
    }

    @Test
    void testSearchAndSortStockNoResults() {
        String title = "NonExisting";
        String category = "Category1";
        Double minPrice = 100.0;
        Double maxPrice = 200.0;
        String sortBy = "title";
        boolean ascending = true;

        List<Stock> result = stockService.searchAndSortStock(title, category, minPrice, maxPrice, sortBy, ascending);

        assertEquals(0, result.size(), "Aucun résultat ne devrait être trouvé");
    }
}
