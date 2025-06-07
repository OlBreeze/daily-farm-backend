package org.dailyfarm.products.service;

import static org.dailyfarm.service.api.ExceptionMessageConstants.*;
import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.ProductDto;
import org.dailyfarm.products.entities.Category;
import org.dailyfarm.products.entities.Product;
import org.dailyfarm.products.entities.Unit;
import org.dailyfarm.products.repo.ProductsRepository;
import org.dailyfarm.products.service.mappers.PagedMapper;
import org.dailyfarm.products.service.mappers.ProductMapper;
import org.dailyfarm.products.service.pagination.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements IProductsService {

	private final ProductMapper productMapper;
	private final ProductsRepository productsRepo;

	@Override
	public List<ProductDto> getAllProducts() {
	    return productsRepo.findAll().stream()
	        .map(productMapper::toDto)
	        .toList();
	}

	@Transactional
	@Override
	public void addProduct(ProductDto dto) {
		Product entity = productMapper.toEntity(dto);
//		product.setId(UUID.randomUUID());
		productsRepo.save(entity);
	}

	@Override
	public ProductDto getProduct(String name) {
		if (name == null)
			throw new IllegalArgumentException(WRONG_ARGUMENT);

		Product pr = productsRepo.findByName(name);

		if (pr == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_EXISTS);

		return productMapper.toDto(pr);
	}

	@Override
	public ProductDto getProduct(UUID id) {
		if (id == null)
			throw new IllegalArgumentException(WRONG_ARGUMENT);

		Product pr = productsRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_EXISTS));

		return productMapper.toDto(pr);
	}
	
	@Transactional
	@Override
	public ProductDto removeProduct(UUID id) {
		Product pr = productsRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_EXISTS));
		productsRepo.delete(pr);
		return productMapper.toDto(pr);
	}

	@Transactional
	public ProductDto updateProduct(UUID id, ProductDto dto) {
	    Product existingProduct = productsRepo.findById(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRODUCT_NOT_EXISTS));

	    if (dto.getName()!=null)
	    	existingProduct.setName(dto.getName());

	    if (dto.getPrice()!=null)
	    	existingProduct.setPrice(dto.getPrice());
	    
	    if (dto.getQuantity()!=null)
	    	existingProduct.setQuantity(dto.getQuantity());
	    
	    if (dto.getComment()!=null)
	    	existingProduct.setComment(dto.getComment());
	    
	    //  category
	    if (dto.getCategoryId() != null) {
	        Category category = new Category();
	        category.setId(dto.getCategoryId());
	        existingProduct.setCategory(category);
	    } else {
	       // existingProduct.setCategory(null);
	    }

	    //  unit
	    if (dto.getUnitId() != null) {
	        Unit unit = new Unit();
	        unit.setId(dto.getUnitId());
	        existingProduct.setUnit(unit);
	    } else {
	       // existingProduct.setUnit(null);
	    }

	    productsRepo.save(existingProduct);
	    return productMapper.toDto(existingProduct);
	}
	
	@Override
	public
	PagedModel<ProductDto> getProductsPages(Integer pageNumber, Integer pageSize, String search) {
		Pageable pageRequest = PageUtil.getPagination(pageNumber, pageSize);
		Page<Product> page = productsRepo.findProducts(search, pageRequest);
		return PagedMapper.map(page, productMapper::toDto);
	}
}
