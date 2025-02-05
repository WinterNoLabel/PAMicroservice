package org.backend.pamicroservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.backend.pamicroservice.models.dto.ProfileResponseDTO;
import org.backend.pamicroservice.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Личный кабинет пользователя")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    @Operation(summary = "Возвращает профиль пользователя")
    public ResponseEntity<?> getProfile(@RequestParam Long userID) {
        ProfileResponseDTO profileResponseDTO = profileService.getProfile(userID);

        if (profileResponseDTO == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("detail", "Пользователь с ID " + userID + " не найден");

            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(profileResponseDTO);
    }

    @PatchMapping(value = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Частичное обновление профиля")
    public ResponseEntity<ProfileResponseDTO> partialUpdateProfile(@RequestParam Long userID,
                                                                   @RequestParam(required = false) String username,
                                                                   @RequestParam(required = false) String firstName,
                                                                   @RequestPart(required = false) MultipartFile photo) {
        return ResponseEntity.ok(profileService.updateProfile(userID, username, firstName, photo));
    }
}
