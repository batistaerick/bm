package com.budgetmanager.bm.services;

import com.budgetmanager.bm.domain.entities.UserImage;
import com.budgetmanager.bm.exceptions.GlobalException;
import com.budgetmanager.bm.repositories.UserImageRepository;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final UserImageRepository repository;
    private final UserService userService;

    @Transactional
    public void updateUserImage(MultipartFile file) {
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
        image.setName(file.getName());
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
}
