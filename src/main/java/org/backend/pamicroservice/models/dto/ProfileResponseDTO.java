package org.backend.pamicroservice.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponseDTO {
    Long id;
    String username;
    String firstName;
    String photoUrl;
}
