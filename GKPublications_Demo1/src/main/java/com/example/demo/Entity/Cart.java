package com.example.demo.Entity;

import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>(); // List of CartItems to support duplicate books

    private Double subtotal;
    private Double total;

    public void calculateTotals() {
        this.subtotal = cartItems.stream()
                                 .mapToDouble(CartItem::getSubtotal)
                                 .sum();
        this.total = subtotal; // Modify if you want to apply discounts or additional charges.
    }
}