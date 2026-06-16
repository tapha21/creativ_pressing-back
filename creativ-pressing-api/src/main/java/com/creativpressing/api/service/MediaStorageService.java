package com.creativpressing.api.service;

import com.creativpressing.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class MediaStorageService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final Path mediaDirectory;
    private final String contextPath;

    public MediaStorageService(@Value("${app.media.upload-dir:media}") String uploadDir,
            @Value("${server.servlet.context-path:}") String contextPath) {
        this.mediaDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.contextPath = contextPath;
        createMediaDirectory();
    }

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Image obligatoire");
        }

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        Path target = mediaDirectory.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return contextPath + "/media/" + fileName;
        } catch (IOException e) {
            throw new BusinessException("Impossible d'enregistrer l'image");
        }
    }

    public Path getMediaDirectory() {
        return mediaDirectory;
    }

    private void createMediaDirectory() {
        try {
            Files.createDirectories(mediaDirectory);
        } catch (IOException e) {
            throw new BusinessException("Impossible de creer le dossier media");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BusinessException("Format d'image invalide");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("Seules les images jpg, jpeg, png et webp sont acceptees");
        }

        return extension;
    }
}
