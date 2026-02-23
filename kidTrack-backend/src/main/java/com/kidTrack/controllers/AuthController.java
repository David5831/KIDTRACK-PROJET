package com.kidTrack.controllers;

import com.kidTrack.dto.AuthResponse;
import com.kidTrack.dto.LoginRequest;
import com.kidTrack.models.*;
import com.kidTrack.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    
    @PostMapping("/register/parent")
    public ResponseEntity<User> registerParent(@RequestBody Parent parent) {
        return ResponseEntity.ok(authService.registerParent(parent));
    }
    
    @PostMapping("/register/educator")
    public ResponseEntity<User> registerEducator(@RequestBody Educator educator) {
        return ResponseEntity.ok(authService.registerEducator(educator));
    }
    
    @PostMapping("/register/admin")
    public ResponseEntity<User> registerAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(authService.registerAdmin(admin));
    }
}
