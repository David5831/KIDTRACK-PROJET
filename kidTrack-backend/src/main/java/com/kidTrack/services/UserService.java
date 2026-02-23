package com.kidTrack.services;

import com.kidTrack.models.*;
import com.kidTrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Map<String, Object>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getUsersByRole(String role) {
        List<User> users = userRepository.findAll();
        return users.stream()
            .filter(user -> getUserRole(user).equals(role))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<Map<String, Object>> getUserById(Integer id) {
        return userRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public Map<String, Object> updateUser(Integer id, Map<String, Object> userData) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (userData.containsKey("firstName")) {
            user.setFirstName((String) userData.get("firstName"));
        }
        if (userData.containsKey("lastName")) {
            user.setLastName((String) userData.get("lastName"));
        }
        if (userData.containsKey("email")) {
            user.setEmail((String) userData.get("email"));
        }
        if (userData.containsKey("phone")) {
            user.setPhone((String) userData.get("phone"));
        }
        if (userData.containsKey("password")) {
            user.setPassword(passwordEncoder.encode((String) userData.get("password")));
        }
        if (userData.containsKey("profileImage")) {
            user.setProfileImage((String) userData.get("profileImage"));
        }
        
        // Special fields for Admin
        if (user instanceof Admin && userData.containsKey("superAdmin")) {
            ((Admin) user).setSuperAdmin((Boolean) userData.get("superAdmin"));
        }
        
        // Special fields for Educator - update username if email changes
        if (user instanceof Educator && userData.containsKey("email")) {
            String username = ((String) userData.get("email")).split("@")[0];
            ((Educator) user).setUsername(username);
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    public Map<String, Object> createUser(Map<String, Object> userData) {
        String role = (String) userData.get("role");
        User user;
        
        // Create the appropriate user type based on role
        switch (role) {
            case "PARENT":
                user = new Parent();
                break;
            case "EDUCATOR":
                Educator educator = new Educator();
                // Set username as email (before @ symbol) for educators
                String username = ((String) userData.get("email")).split("@")[0];
                educator.setUsername(username);
                user = educator;
                break;
            case "ADMIN":
                user = new Admin();
                if (userData.containsKey("superAdmin")) {
                    ((Admin) user).setSuperAdmin((Boolean) userData.get("superAdmin"));
                }
                break;
            default:
                throw new RuntimeException("Invalid role: " + role);
        }
        
        // Set common fields
        user.setFirstName((String) userData.get("firstName"));
        user.setLastName((String) userData.get("lastName"));
        user.setEmail((String) userData.get("email"));
        user.setPhone((String) userData.get("phone"));
        user.setPassword(passwordEncoder.encode((String) userData.get("password")));
        
        if (userData.containsKey("profileImage")) {
            user.setProfileImage((String) userData.get("profileImage"));
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    
    private Map<String, Object> convertToDTO(User user) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", user.getId());
        dto.put("firstName", user.getFirstName());
        dto.put("lastName", user.getLastName());
        dto.put("email", user.getEmail());
        dto.put("phone", user.getPhone());
        dto.put("profileImage", user.getProfileImage());
        dto.put("role", getUserRole(user));
        dto.put("createdAt", user.getCreatedAt());
        dto.put("updatedAt", user.getUpdatedAt());
        
        // Add role-specific fields
        if (user instanceof Admin) {
            dto.put("superAdmin", ((Admin) user).getSuperAdmin());
        } else if (user instanceof Educator) {
            dto.put("username", ((Educator) user).getUsername());
        }
        
        return dto;
    }
    
    private String getUserRole(User user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof Educator) {
            return "EDUCATOR";
        } else if (user instanceof Parent) {
            return "PARENT";
        }
        return "UNKNOWN";
    }
}
