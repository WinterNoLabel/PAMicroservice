package org.backend.pamicroservice.models.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "user_id")
    private Long userId;

}
