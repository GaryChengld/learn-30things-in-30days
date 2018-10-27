package io.examples.spring.rest.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Gary Cheng
 */
@Service
@Slf4j
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileProperties fileProperties) {
        log.debug("upload directory:{}", fileProperties.getUploadDir());
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        if (!Files.exists(this.fileStorageLocation)) {
            log.debug("Upload directory not exists");
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory.", ex);
            }
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.debug("storeFile filename:{}", fileName);
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Store file " + fileName + "failed", ex);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }
}
