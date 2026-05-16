package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.UserImage;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.UserImageRepository;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.rowset.serial.SerialBlob;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        "image/webp"
    );

    private final UserImageRepository repository;
    private final UserService userService;

    @Value("${app.uploads.max-profile-image-bytes}")
    private long maxProfileImageBytes;

    @Transactional
    public void updateUserImage(MultipartFile file) {
        validateImage(file);

        UserImage image = repository
            .findByUserEmail(userService.getCurrentUsername())
            .orElse(new UserImage());

        try {
            Blob blob = new SerialBlob(file.getBytes());
            image.setProfileImage(blob);
        } catch (IOException | SQLException exception) {
            throw new GlobalException(
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Invalid image {}",
                exception.getMessage()
            );
        }
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setUser(
            userService
                .getCurrentUser()
                .orElseThrow(() ->
                    new UsernameNotFoundException("User Not Found")
                )
        );

        repository.save(image);
    }

    @Transactional(readOnly = true)
    public byte[] findByUserEmail() {
        UserImage image = repository
            .findByUserEmail(userService.getCurrentUsername())
            .orElseThrow(() ->
                new GlobalException(HttpStatus.NOT_FOUND, "Not found.")
            );
        try {
            return image
                .getProfileImage()
                .getBytes(1, (int) image.getProfileImage().length());
        } catch (Exception exception) {
            throw new GlobalException(
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error getting profile image {}",
                exception
            );
        }
    }

    public String findContentTypeByUserEmail() {
        return repository
            .findByUserEmail(userService.getCurrentUsername())
            .map(UserImage::getType)
            .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GlobalException(
                HttpStatus.BAD_REQUEST,
                "Image file is required"
            );
        }
        if (file.getSize() > maxProfileImageBytes) {
            throw new GlobalException(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Image exceeds the maximum allowed size"
            );
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new GlobalException(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Only JPEG, PNG, and WEBP images are supported"
            );
        }
    }
}
