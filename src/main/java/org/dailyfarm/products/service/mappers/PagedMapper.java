package org.dailyfarm.products.service.mappers;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PagedMapper {
	public static <T, R> R map(T input, Function<T, R> mapper) {
		return mapper.apply(input);
	}
	
	public static <T, R> List<R> map(List<T> input, Function<T, R> mapper) {
		return input.stream().map(mapper::apply).toList();
	}
	
	public static <T, R> PagedModel<R> map(Page<T> input, Function<T, R> mapper) {
		return new PagedModel<R>(input.map(mapper));
	}
}
