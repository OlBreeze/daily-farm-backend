package org.dailyfarm.products.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class ProductDto {

	private UUID id;
	private UUID userId;
	private String username;
	
	private UUID categoryId;
	private String categoryName;
	
	private UUID unitId;
	private String unitName;
	
	private String name;
	private String comment;
	private Double price;
	private Double quantity;
}
