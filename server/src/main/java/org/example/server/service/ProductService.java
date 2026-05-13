package org.example.server.service;

import jakarta.transaction.Transactional;
import org.example.server.model.Image;
import org.example.server.model.Product;
import org.example.server.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Transactional
    public Product getProductById(int id) {
        // sending null is not considered a good practice
//        return repo.findById(id).orElse(null);
        return repo.findById(id).orElse(new Product(-1));
    }

    @Transactional
    public void addOrUpdateProduct(Product product, MultipartFile image) throws Exception {
        Image img = new Image();
        img.setImageName(image.getOriginalFilename());
        img.setImageType(image.getContentType());
        img.setImageData(image.getBytes());
        product.setImage(img);
        repo.save(product);
    }

    public void deleteById(int id) {
        repo.deleteById(id);
    }

    @Transactional
    public Image getImageById(int productId) {
        Product product = repo.findById(productId).orElse(new Product(-1));
        return product.getImage();
    }

    @Transactional
    public List<Product> search(String keyword) {
        return repo.findAllByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, keyword);
    }

    @Transactional
    public void loadProducts() {
        try {
            ClassPathResource resource = new ClassPathResource("insert-data.sql");
            String sql = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            jdbcTemplate.update(sql);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load products from SQL file", e);
        }
    }
}
