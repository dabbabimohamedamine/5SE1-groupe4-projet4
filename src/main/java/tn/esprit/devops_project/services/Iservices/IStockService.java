package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Stock;

import java.util.List;

public interface IStockService {

    Stock addStock(Stock stock);

    Stock retrieveStock(Long id);

    List<Stock> retrieveAllStock();

    // Recherche et tri des stocks
    List<Stock> searchAndSortStock(String title, String category, Double minPrice, Double maxPrice, String sortBy, boolean ascending);
}
