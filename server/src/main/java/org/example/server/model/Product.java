package org.example.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // this will automatically generate value for id
    private int id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private String category;
    @JsonFormat(shape =  JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy")
    private Date releaseDate;
    private boolean productAvailable;
    private int stockQuantity;

    // To automatically persist the Image when saving a Product, add cascade = CascadeType.ALL
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    public Product(int id) {
        this.id = id;
    }
}
