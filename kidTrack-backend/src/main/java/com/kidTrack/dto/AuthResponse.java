package com.kidTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private Integer userId;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private Integer groupId;
    
    public AuthResponse(String token, Integer userId, String email, String role, String firstName, String lastName) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public AuthResponse(String token, Integer userId, String email, String role, String firstName, String lastName, Integer groupId) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }
}
