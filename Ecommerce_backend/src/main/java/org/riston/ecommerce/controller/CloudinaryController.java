package org.riston.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.riston.ecommerce.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) throws IOException {
        String url = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.ok(url);
    }
}