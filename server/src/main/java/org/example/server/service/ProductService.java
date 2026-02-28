package org.example.server.service;

import jakarta.transaction.Transactional;
import org.example.server.model.Image;
import org.example.server.model.Product;
import org.example.server.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

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
}
