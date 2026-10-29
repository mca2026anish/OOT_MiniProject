package com.cleangoa.cleangoa;

import com.cleangoa.cleangoa.service.ImageStorageService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ImageController {

private final ImageStorageService imageStorageService;

public ImageController(ImageStorageService imageStorageService) {
    this.imageStorageService = imageStorageService;
}

@GetMapping("/images/{filename:.+}")
public ResponseEntity<Resource> viewImage(
        @PathVariable String filename) throws IOException {

    Path imagePath = imageStorageService.getImagePath(filename);

    if (!Files.isRegularFile(imagePath)
            || !Files.isReadable(imagePath)) {
        return ResponseEntity.notFound().build();
    }

    String contentType = Files.probeContentType(imagePath);

    if (contentType == null
            || !(contentType.equals("image/jpeg")
            || contentType.equals("image/png")
            || contentType.equals("image/webp"))) {
        return ResponseEntity.notFound().build();
    }

    Resource resource = new UrlResource(imagePath.toUri());

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
            .header("X-Content-Type-Options", "nosniff")
            .body(resource);
}

}
