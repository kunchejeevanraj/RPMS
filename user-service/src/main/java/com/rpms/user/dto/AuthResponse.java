package com.rpms.user.dto;

//upodation
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String email;
    private String role;
    
    //updation
    private String accessToken;
    private String refreshToken; // We are not implementing refresh now, but keep it
    private String tokenType;
    private UUID userId;
    //private String email;
    private String firstName;
    private String lastName;
    //private String role;
    //private String message;
    private boolean success;
    private LocalDateTime timestamp;
}