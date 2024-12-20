package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Para exponer las imagenes en la vista
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + userPhotosDir + "/**").addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesDirName = "../category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);
        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoryImagesPath + "/");
    }
}
