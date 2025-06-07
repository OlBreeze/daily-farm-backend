package org.dailyfarm.products.controllers;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.ProductDto;
import org.dailyfarm.products.service.IProductsService;
import org.dailyfarm.service.constants.DefaultConstants;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.dailyfarm.service.api.ApiConstants.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
//@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductsController {
	
	private final IProductsService productService;
	
	// localhost:8080/products/pages?pageNumber=1&pageSize=3&search=cgn
	
	@GetMapping(GET_PAGES_PRODUCTS)
	public PagedModel<ProductDto> getProductsPages(
			@RequestParam(required = false, defaultValue = DefaultConstants.DEFAULT_PAGE_NUMBER + "") Integer pageNumber,
			@RequestParam(required = false, defaultValue = DefaultConstants.DEFAULT_PAGE_SIZE + "") Integer pageSize,
			@RequestParam(required = false) String search) {
		//log.info("Ищу продукты по фразе: {}", search);
		
		return productService.getProductsPages(pageNumber, pageSize, search);
	}
	
	@GetMapping(GET_ALL_PRODUCTS)
	List<ProductDto> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping(GET_PRODUCT_ID)
	public ProductDto getProduct(@PathVariable UUID id) {
	    return productService.getProduct(id);
	}
	
	@GetMapping(GET_PRODUCT_NAME)
	public ProductDto getProduct_name(@PathVariable String name) {
	    return productService.getProduct(name);
	}
	
	@PostMapping(ADD_PRODUCT)
	public void addProduct(@RequestBody ProductDto product) {
		productService.addProduct(product);
	}
	
	@DeleteMapping(REMOVE_PRODUCT)
	public ProductDto removeProduct(@PathVariable UUID id) {
		return productService.removeProduct(id);
	}
	
	@PutMapping(UPDATE_PRODUCT)
	public ProductDto updateProduct(@PathVariable UUID id, @RequestBody ProductDto dto) {
	    return productService.updateProduct(id, dto);
	}
	
}


