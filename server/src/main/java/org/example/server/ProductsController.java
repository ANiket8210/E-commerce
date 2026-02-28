package org.example.server;


import org.example.server.model.Image;
import org.example.server.model.Product;
import org.example.server.response.APIResponse;
import org.example.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin
public class ProductsController {

    @Autowired
    ProductService productService;

    @GetMapping("products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product =  productService.getProductById(id);
        if (product.getId() > 0){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("product")
    public ResponseEntity<APIResponse<Product>> createProduct(@RequestPart MultipartFile imageFile, @RequestPart Product product){
        try{
            productService.addOrUpdateProduct(product,imageFile);
            APIResponse<Product> response = new APIResponse<>("Product created successfully", product);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            System.out.println("Error in inserting product"+ e.getMessage());
            APIResponse<Product> errorResponse = new APIResponse<>("Failed to create product: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable int id){
        Product product =  productService.getProductById(id);
        if(product.getId() > 0){
        productService.deleteById(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByName(@PathVariable int productId){
        Image image = productService.getImageById(productId);
        if (image != null){
        return new ResponseEntity<>(image.getImageData(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<APIResponse<Product>> updateProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile){
        try{
            productService.addOrUpdateProduct(product,imageFile);
            APIResponse<Product> response = new APIResponse<>("Product created successfully", product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("Error in inserting product"+ e.getMessage());
            APIResponse<Product> errorResponse = new APIResponse<>("Failed to create product: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products  = productService.search(keyword);
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
