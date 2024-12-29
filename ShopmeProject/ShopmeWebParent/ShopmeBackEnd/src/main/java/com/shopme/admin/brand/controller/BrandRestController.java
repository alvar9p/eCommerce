package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.exception.BrandNotFoundRestException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name){
        return brandService.checkUnique(id,name);
    }

    // List collection of category object
    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundException, BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();

        try{
            Brand brand = brandService.get(brandId);
            Set<Category> categories = brand.getCategories();
            for (Category category : categories){
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(categoryDTO);
            }

            return listCategories;
        }catch (BrandNotFoundException ex){
            throw new BrandNotFoundRestException();
        }

    }

}
