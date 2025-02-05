package org.backend.pamicroservice.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.backend.pamicroservice.models.dto.ProfileResponseDTO;
import org.backend.pamicroservice.models.entity.Profile;
import org.backend.pamicroservice.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Transactional
    public Profile createUserProfile(Long userId, String username) {
        Profile profile = Profile.builder()
                .userId(userId)
                .username(username)
                .build();

        return profileRepository.save(profile);
    }


    public ProfileResponseDTO getProfile(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);

        if (profile == null) {
            return null;
        }

        return ProfileResponseDTO.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .photoUrl(profile.getPhotoUrl()).build();

    }

    public ProfileResponseDTO updateProfile(Long userId, String username, String firstName, MultipartFile photo) {
        Profile profile = profileRepository.findByUserId(userId);

        if (profile == null) {
            return null;
        }

        if (username != null) {
            profile.setUsername(username);
        }
        if (firstName != null) {
            profile.setFirstName(firstName);
        }
        if (photo != null && !photo.isEmpty()) {
            try {
                String photoUrl =uploadPhotoToMinio(userId, photo);
                profile.setPhotoUrl(photoUrl);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка загрузки фото: " + e.getMessage());
            }
        }
        profileRepository.save(profile);

        return ProfileResponseDTO.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .photoUrl(profile.getPhotoUrl())
                .build();
    }

    public String uploadPhotoToMinio(Long userId, MultipartFile file) throws Exception {
        String fileName = "profile_" + userId + "_" + UUID.randomUUID() + ".jpg";

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Ошибка загрузки файла в MinIO: " + e.getMessage());
        }

        return String.format("%s/%s/%s", minioUrl, bucketName, fileName);
    }
}
