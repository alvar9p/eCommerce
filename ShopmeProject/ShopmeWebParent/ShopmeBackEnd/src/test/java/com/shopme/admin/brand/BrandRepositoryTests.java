package com.shopme.admin.brand;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testCreateBrand1(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");
        acer.getCategories().add(laptops);

        Brand savedBrand = brandRepository.save(acer);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2(){
        Category cellphones = new Category(4);
        Category tables = new Category(7);

        Brand apple = new Brand("Apple");
        apple.getCategories().add(cellphones);
        apple.getCategories().add(tables);

        Brand savedBrand = brandRepository.save(apple);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand3(){
        Brand samsung = new Brand("Samsung");
        samsung.getCategories().add(new Category(29)); // category memory
        samsung.getCategories().add(new Category(24)); // category internal hard drive

        Brand savedBrand = brandRepository.save(samsung);
        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll(){
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById(){
        Brand brand = brandRepository.findById(1).get();

        assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdateName(){
        String newName = "Samsung Electronics";
        Brand samsung = brandRepository.findById(3).get();
        samsung.setName(newName);

        Brand savedBrand = brandRepository.save(samsung);
        assertThat(savedBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete(){
        Integer id = 2;
        brandRepository.deleteById(id);

        Optional<Brand> result = brandRepository.findById(id);
        assertThat(result.isEmpty());
    }

}
