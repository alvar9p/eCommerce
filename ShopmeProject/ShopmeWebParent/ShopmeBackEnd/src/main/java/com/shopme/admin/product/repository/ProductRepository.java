package com.shopme.admin.product.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Product findByName(String name);

    public Long countById(Integer id);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
