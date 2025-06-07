package org.dailyfarm.products.service.pagination;

import org.dailyfarm.service.constants.DefaultConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class PageUtil {

	public static Pageable getPagination(Integer pageNumber, Integer pageSize) {
		if (pageNumber == null || pageNumber < DefaultConstants.DEFAULT_PAGE_NUMBER) {
			pageNumber = DefaultConstants.DEFAULT_PAGE_NUMBER;
		}
		if (pageSize == null || pageSize < DefaultConstants.DEFAULT_PAGE_SIZE) {
			pageSize = DefaultConstants.DEFAULT_PAGE_SIZE;
		}
		
		Pageable pageRequest = PageRequest.of(pageNumber - 1, pageSize);
		return pageRequest;
	}
}
