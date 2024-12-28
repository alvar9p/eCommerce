package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.exception.BrandNotFoundException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model){
        List<Brand> listBrands = brandService.listAll();
        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesUserInForm();
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) throws IOException{
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = brandService.save(brand);
            String uploadDir = "../brand-logos/" + savedBrand.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
           brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUserInForm();
            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
            return "brands/brand_form";
        }catch (BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            brandService.delete(id);
            String uploadDir = "../brand-logos/" + id;
            FileUploadUtil.removeDir(uploadDir);
            redirectAttributes.addFlashAttribute("message",
                    "The brand ID " + id + " has been deleted successfully");
        }catch (BrandNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/brands";
    }

}
