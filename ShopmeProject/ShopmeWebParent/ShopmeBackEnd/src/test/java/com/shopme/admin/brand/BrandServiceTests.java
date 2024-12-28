package com.shopme.admin.brand;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.common.entity.Brand;
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
public class BrandServiceTests {

    @MockBean private BrandRepository brandRepository;

    @InjectMocks private BrandService brandService;

    @Test
    public void testCheckUniqueInNewModeReturnDuplicate(){
        Integer id = null;
        String name = "Acer";
        Brand brand = new Brand(name);
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
        String result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK(){
        Integer id = 1;
        String name = "AMD";
        Mockito.when(brandRepository.findByName(name)).thenReturn(null);
        String result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testCheckUniqueInEditModeReturnDuplicate(){
        Integer id = 1;
        String name = "Canon";
        Brand brand = new Brand(id, name);
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
        String result = brandService.checkUnique(2, "Canon");
        assertThat(result).isEqualTo("Duplicate");
    }

    @Test
    public void testCheckUniqueInEditModeReturnOK(){
        Integer id = 1;
        String name = "Acer";
        Brand brand = new Brand(id, name);
        Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
        String result = brandService.checkUnique(id, name);
        assertThat(result).isEqualTo("OK");
    }

}
