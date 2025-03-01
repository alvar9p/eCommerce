package com.shopme.admin.brand.service;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    public static final int BRANDS_PER_PAGE = 10;

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll(){
        return (List<Brand>) brandRepository.findAll();
    }

    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
        if (keyword != null){
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException{
        try{
            return brandRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new BrandNotFoundException("Could not find any brand with ID: " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException{
        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0){
            throw new BrandNotFoundException("Could not find any brand with ID: " + id);
        }
        brandRepository.deleteById(id);
    }

    // Estos mensajes se comparan con las funciones de JS
    public String checkUnique(Integer id, String name){
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = brandRepository.findByName(name);
        if (isCreatingNew) {
            if (brandByName != null) return "Duplicate";
        }else {
            if (brandByName != null && brandByName.getId() != id){
                return "Duplicate";
            }
        }
        return "OK";
    }
}
