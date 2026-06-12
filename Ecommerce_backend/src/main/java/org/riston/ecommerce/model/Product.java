package org.riston.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int sellingPrice;

    private int mrpPrice;

    private int discountPercent;

    private int quantity;

    private String color;
    @ElementCollection
    private List<String> images = new ArrayList<>();

    private int numRatings;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Seller seller;

    private LocalDateTime createdAt;

    private String sizes;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<Reviews> reviews = new ArrayList<>();
}