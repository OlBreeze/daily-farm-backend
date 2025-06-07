package org.dailyfarm.products.service;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.ProductDto;
import org.springframework.data.web.PagedModel;

public interface IProductsService {
	List<ProductDto> getAllProducts();
	
	ProductDto getProduct(String name);
	ProductDto getProduct(UUID id);
	void addProduct(ProductDto product);
//	ProductDto removeProduct(ProductDto product);
	ProductDto removeProduct(UUID id);
	ProductDto updateProduct(UUID id, ProductDto dto);

	PagedModel<ProductDto> getProductsPages(Integer pageNumber, Integer pageSize, String search);

	//ProductsBuyCallResponseDto buyProduct(BuyProductRequestDto buyRequest); 
}
