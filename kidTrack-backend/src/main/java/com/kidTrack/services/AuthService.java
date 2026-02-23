package com.kidTrack.services;

import com.kidTrack.dto.AuthResponse;
import com.kidTrack.dto.LoginRequest;
import com.kidTrack.models.*;
import com.kidTrack.repositories.*;
import com.kidTrack.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ParentRepository parentRepository;
    
    @Autowired
    private EducatorRepository educatorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Tentative de connexion pour l'email: {}", loginRequest.getEmail());
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        logger.info("Utilisateur trouvé - Email: {}, Type: {}", user.getEmail(), user.getClass().getSimpleName());
        logger.debug("Mot de passe reçu (longueur): {}", loginRequest.getPassword().length());
        logger.debug("Hash en BDD (début): {}", user.getPassword().substring(0, 20));
        
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        logger.info("Résultat de la comparaison: {}", matches);
        
        if (!matches) {
            logger.error("Échec de l'authentification - mot de passe incorrect");
            throw new RuntimeException("Invalid credentials");
        }
        
        String role = determineUserRole(user);
        String token = tokenProvider.generateToken(user.getEmail(), role);
        
        // Récupérer le groupId si c'est un éducateur sans charger l'entité Group complète
        Integer groupId = null;
        if (user instanceof Educator) {
            groupId = educatorRepository.findGroupIdByEducatorId(user.getId());
        }
        
        logger.info("Connexion réussie - Role: {}, GroupId: {}", role, groupId);
        
        return new AuthResponse(token, user.getId(), user.getEmail(), role, 
                user.getFirstName(), user.getLastName(), groupId);
    }
    
    public User registerParent(Parent parent) {
        if (userRepository.existsByEmail(parent.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        return parentRepository.save(parent);
    }
    
    public User registerEducator(Educator educator) {
        if (userRepository.existsByEmail(educator.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        educator.setPassword(passwordEncoder.encode(educator.getPassword()));
        return educatorRepository.save(educator);
    }
    
    public User registerAdmin(Admin admin) {
        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    
    private String determineUserRole(User user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof Educator) {
            return "EDUCATOR";
        } else if (user instanceof Parent) {
            return "PARENT";
        }
        return "USER";
    }
}
