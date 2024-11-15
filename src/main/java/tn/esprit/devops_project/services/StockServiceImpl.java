package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.Iservices.IStockService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockServiceImpl implements IStockService {

    private final StockRepository stockRepository;

    @Override
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock retrieveStock(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new NullPointerException("Stock not found"));
    }

    @Override
    public List<Stock> retrieveAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public List<Stock> searchAndSortStock(String title, String category, Double minPrice, Double maxPrice, String sortBy, boolean ascending) {
        List<Stock> filteredStocks = stockRepository.findAll().stream()
                .filter(stock -> title == null || stock.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(stock -> category == null || stock.getCategory().equalsIgnoreCase(category))
                .filter(stock -> minPrice == null || stock.getPrice() >= minPrice)
                .filter(stock -> maxPrice == null || stock.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        filteredStocks.sort((s1, s2) -> {
            int comparison = 0;

            if (sortBy != null) {
                if (sortBy.equalsIgnoreCase("title")) {
                    comparison = s1.getTitle().compareTo(s2.getTitle());
                } else if (sortBy.equalsIgnoreCase("price")) {
                    comparison = s1.getPrice().compareTo(s2.getPrice());
                } else if (sortBy.equalsIgnoreCase("category")) {
                    comparison = s1.getCategory().compareTo(s2.getCategory());
                }
            }


            if (ascending) {
                return comparison;
            } else {
                return -comparison;
            }
        });

        return filteredStocks;
    }


}
