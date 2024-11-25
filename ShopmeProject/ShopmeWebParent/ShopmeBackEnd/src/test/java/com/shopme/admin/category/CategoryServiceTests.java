package com.shopme.admin.category;

import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    // Crea un fake Repo de esta clase
    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

     @Test
    public void testCheckUniqueInNewModeReturnDuplicateName(){
        Integer id = null;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateName");
     }

     @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias(){
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateName(){
        Integer id = 1;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicateAlias(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";
        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("OK");
    }

}
