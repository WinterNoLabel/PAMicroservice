package org.backend.pamicroservice.models.dto;

import lombok.Data;

@Data
public class UserRegisteredEvent {
    private Long userId;
    private String username;

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
