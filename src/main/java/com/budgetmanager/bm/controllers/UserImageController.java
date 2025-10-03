package com.budgetmanager.bm.controllers;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import com.budgetmanager.bm.services.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user-image")
@RequiredArgsConstructor
public class UserImageController {

    private final UserImageService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(
        @RequestParam MultipartFile file
    ) {
        service.updateUserImage(file);
        return noContent().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> getUserImage() {
        byte[] image = service.findByUserEmail();
        return ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
