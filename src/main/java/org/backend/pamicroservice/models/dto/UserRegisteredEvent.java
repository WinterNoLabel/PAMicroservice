package org.backend.pamicroservice.models.dto;

import lombok.Data;

@Data
public class UserRegisteredEvent {
    private Long user_id;
    private String username;

    @Override
    public String toString() {
        return "UserRegisteredEvent{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                '}';
    }
}
