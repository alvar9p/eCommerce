package com.shopme.admin.brand.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Long countById(Integer id);

    public Brand findByName(String name);
}
