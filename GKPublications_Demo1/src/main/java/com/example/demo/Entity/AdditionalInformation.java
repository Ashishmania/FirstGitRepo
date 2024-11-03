package com.example.demo.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInformation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double weight;
	private String dimensions;
	private String publisher;
	private String language;
	private String publishingMonth;
	private Integer publishingYear;
	private Integer totalPages;
	private String edition;
	private String buyFromAmazonLink;

	@OneToOne(mappedBy = "additionalInformation", cascade = CascadeType.ALL)
	private Book book;

}
