package com.shopme.admin.category;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface Category extends PagingAndSortingRepository<com.shopme.common.entity.Category, Integer> {
}
