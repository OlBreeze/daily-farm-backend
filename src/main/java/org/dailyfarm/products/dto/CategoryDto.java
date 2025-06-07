package org.dailyfarm.products.dto;

import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class CategoryDto {
	private UUID id;
	private UUID parentCategoryId;
	private String name;
}
