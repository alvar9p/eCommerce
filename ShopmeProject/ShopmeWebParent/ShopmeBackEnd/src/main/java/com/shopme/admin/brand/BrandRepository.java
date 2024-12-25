package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
}
