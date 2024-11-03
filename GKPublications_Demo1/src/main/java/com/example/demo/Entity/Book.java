package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String language;
	private Integer popularity;
	private String productsku;
	private Double price;
	private Double discountPercentage;
	private Double finalPrice;
	
	@Lob
	@Column(name = "cover_Image", nullable = true)
	private Byte[] coverImageUrl;
	
	private String description;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private BookCategory category; // Done

	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id") // Foreign key for Author
    private Author author; //Done 

	@ManyToMany(mappedBy = "books")
	private List<Order> orders; //done
  
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;
	
	@ManyToOne
	@JoinColumn(name = "distributor_id")
	private Distributor distributor;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "additional_info_id", referencedColumnName = "id") 
    private AdditionalInformation additionalInformation; //Done 

	@OneToMany(mappedBy = "book")
	private Set<Rating> ratings; //Done 
	
	//Calculation of final price on the basis of DiscountPercentage
	private void calculateFinalPrice() {
        if (price != null && discountPercentage != null) {
            finalPrice = price - (price * discountPercentage / 100);
        } else {
            finalPrice = price;
        }
    }

    // Setters with final price calculation logic
    public void setPrice(Double price) {
        this.price = price;
        calculateFinalPrice();
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateFinalPrice();
    }
    
    //To ensure final price is always calculation before saving it to the database
    @PrePersist
    @PreUpdate
    private void onPersistOrUpdate() {
        calculateFinalPrice();

     System.out.println("This is my first change in git hub");
    }

}