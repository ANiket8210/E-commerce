package org.example.server.service;

import org.example.server.model.Product;
import org.example.server.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        // sending null is not considered a good practice
//        return repo.findById(id).orElse(null);
        return repo.findById(id).orElse(new Product(-1));
    }
}
