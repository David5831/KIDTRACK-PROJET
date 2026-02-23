package com.kidTrack.controllers;

import com.kidTrack.models.Admin;
import com.kidTrack.models.Educator;
import com.kidTrack.models.Parent;
import com.kidTrack.repositories.AdminRepository;
import com.kidTrack.repositories.EducatorRepository;
import com.kidTrack.repositories.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-status")
@CrossOrigin(origins = "*")
public class UserStatusController {
    
    @Autowired
    private ParentRepository parentRepository;
    
    @Autowired
    private EducatorRepository educatorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    /**
     * Update lastSeen for any user (parent, educator, or admin)
     */
    @PutMapping("/{role}/{userId}")
    public ResponseEntity<Map<String, Object>> updateLastSeen(
            @PathVariable String role,
            @PathVariable Integer userId) {
        
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> response = new HashMap<>();
        
        try {
            switch (role.toUpperCase()) {
                case "PARENT":
                    Parent parent = parentRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Parent not found"));
                    parent.setLastSeen(now);
                    parentRepository.save(parent);
                    break;
                    
                case "EDUCATOR":
                    Educator educator = educatorRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Educator not found"));
                    educator.setLastSeen(now);
                    educatorRepository.save(educator);
                    break;
                    
                case "ADMIN":
                    Admin admin = adminRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
                    admin.setLastSeen(now);
                    adminRepository.save(admin);
                    break;
                    
                default:
                    response.put("error", "Invalid role: " + role);
                    return ResponseEntity.badRequest().body(response);
            }
            
            response.put("success", true);
            response.put("lastSeen", now);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
