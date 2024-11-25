package com.shopme.admin.category.controller;

import com.shopme.admin.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name,
                              @Param("alias") String alias){
        return categoryService.checkUnique(id, name, alias);
    }
}
