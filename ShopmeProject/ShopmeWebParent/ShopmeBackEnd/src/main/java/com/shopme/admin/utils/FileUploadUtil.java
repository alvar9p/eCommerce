package com.shopme.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException exception){
            throw new IOException("Could not save file: " + fileName, exception);
        }
    }

    // Codigo para limpiar el directorio y delar la ultima imagen
    public static void cleanDirectory(String directory){
        Path directoryPath = Paths.get(directory);

        try{
            Files.list(directoryPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    }catch (IOException ex){
                        LOGGER.error("Could not delete file: " + file);
                        //System.out.println("Could not delete file: " + file);
                    }
                }
            });
        }catch (IOException ex){
            LOGGER.error("Could not list directory: " + directoryPath);
            //System.out.println("Could not list directory: " + directoryPath);
        }
    }
}
