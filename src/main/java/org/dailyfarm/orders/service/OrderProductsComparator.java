package org.dailyfarm.orders.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.dailyfarm.orders.dto.OrderProductsDto;
import org.dailyfarm.orders.entities.OrderProducts;

public class OrderProductsComparator {

	    public static boolean isChanged(List<OrderProductsDto> dtoList, List<OrderProducts> entityList) {
	        Set<String> dtoSet = toComparableSetFromDto(dtoList);
	        Set<String> entitySet = toComparableSetFromEntity(entityList);
	        return !dtoSet.equals(entitySet);
	    }

	    private static Set<String> toComparableSetFromDto(List<OrderProductsDto> dtoList) {
	        if (dtoList == null) return Collections.emptySet();
	        return dtoList.stream()
	                .map((OrderProductsDto  p) -> p.getProductId() + "|" +
	                          p.getPrice() + "|" +
	                          p.getQuantity() + "|" +
	                          Objects.toString(p.getComment(), ""))
	                .collect(Collectors.toSet());
	    }

	    private static Set<String> toComparableSetFromEntity(List<OrderProducts> entityList) {
	        if (entityList == null) return Collections.emptySet();
	        return entityList.stream()
	                .map((OrderProducts p) -> p.getProduct().getId() + "|" +
	                          p.getPrice() + "|" +
	                          p.getQuantity() + "|" +
	                          Objects.toString(p.getComment(), ""))
	                .collect(Collectors.toSet());
	    }
}
