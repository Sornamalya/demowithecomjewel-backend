package com.example.demowithecomjewel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the product
    @ManyToOne
    @JoinColumn(name = "product_id") // Only this mapping exists
    private Product product;

    private String productName;
    private Double price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public WishlistItem() {}

    // Full constructor
    public WishlistItem(Product product, String productName, Double price, String imageUrl) {
        this.product = product;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Constructor without image
    public WishlistItem(Product product, String productName, Double price) {
        this(product, productName, price, null);
    }

    // Constructor with just product and name
    public WishlistItem(Product product, String productName) {
        this(product, productName, 0.0, null);
    }

    // ================= Getters and Setters =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    // Convenience getter for productId
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
