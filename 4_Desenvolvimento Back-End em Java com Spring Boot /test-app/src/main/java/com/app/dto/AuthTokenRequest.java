package com.app.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenRequest {
    private String username;
    private String password;
}
