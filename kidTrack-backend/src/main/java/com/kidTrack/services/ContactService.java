package com.kidTrack.services;

import com.kidTrack.models.*;
import com.kidTrack.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactService {
    
    @Autowired
    private ParentRepository parentRepository;
    
    @Autowired
    private EducatorRepository educatorRepository;
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    /**
     * Get all educators linked to a parent's children's groups + admins
     */
    public List<Map<String, Object>> getEducatorsForParent(Integer parentId) {
        Parent parent = parentRepository.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent not found"));
        
        List<Map<String, Object>> contacts = new ArrayList<>();
        
        // Add all admins first
        List<Admin> admins = adminRepository.findAll();
        contacts.addAll(admins.stream()
            .map(this::convertAdminToDTO)
            .collect(Collectors.toList()));
        
        // Get all groups from parent's children
        Set<Integer> groupIds = parent.getChildren().stream()
            .filter(child -> child.getGroup() != null)
            .map(child -> child.getGroup().getId())
            .collect(Collectors.toSet());
        
        // Get all educators from those groups
        Set<Educator> educators = new HashSet<>();
        for (Integer groupId : groupIds) {
            educators.addAll(educatorRepository.findByGroupId(groupId));
        }
        
        contacts.addAll(educators.stream()
            .map(this::convertEducatorToDTO)
            .collect(Collectors.toList()));
        
        return contacts;
    }
    
    /**
     * Get all parents of children in an educator's group + admins
     */
    public List<Map<String, Object>> getParentsForEducator(Integer educatorId) {
        Educator educator = educatorRepository.findById(educatorId)
            .orElseThrow(() -> new RuntimeException("Educator not found"));
        
        List<Map<String, Object>> contacts = new ArrayList<>();
        
        // Add all admins first
        List<Admin> admins = adminRepository.findAll();
        contacts.addAll(admins.stream()
            .map(this::convertAdminToDTO)
            .collect(Collectors.toList()));
        
        if (educator.getGroup() == null) {
            return contacts;
        }
        
        // Get all children in educator's group
        List<Children> children = childrenRepository.findByGroupId(educator.getGroup().getId());
        
        // Get unique parents
        Set<Parent> parents = children.stream()
            .map(Children::getParent)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        
        contacts.addAll(parents.stream()
            .map(this::convertParentToDTO)
            .collect(Collectors.toList()));
        
        return contacts;
    }
    
    /**
     * Get all parents (for admin)
     */
    public List<Map<String, Object>> getAllParents() {
        return parentRepository.findAll().stream()
            .map(this::convertParentToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all educators (for admin)
     */
    public List<Map<String, Object>> getAllEducators() {
        return educatorRepository.findAll().stream()
            .map(this::convertEducatorToDTO)
            .collect(Collectors.toList());
    }
    
    private Map<String, Object> convertParentToDTO(Parent parent) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", parent.getId());
        dto.put("firstName", parent.getFirstName());
        dto.put("lastName", parent.getLastName());
        dto.put("email", parent.getEmail());
        dto.put("role", "PARENT");
        dto.put("phone", parent.getPhone());
        dto.put("lastSeen", parent.getLastSeen());
        return dto;
    }
    
    private Map<String, Object> convertEducatorToDTO(Educator educator) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", educator.getId());
        dto.put("firstName", educator.getFirstName());
        dto.put("lastName", educator.getLastName());
        dto.put("email", educator.getEmail());
        dto.put("role", "EDUCATOR");
        if (educator.getGroup() != null) {
            dto.put("groupName", educator.getGroup().getName());
        }
        dto.put("lastSeen", educator.getLastSeen());
        return dto;
    }
    
    private Map<String, Object> convertAdminToDTO(Admin admin) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", admin.getId());
        dto.put("firstName", admin.getFirstName());
        dto.put("lastName", admin.getLastName());
        dto.put("email", admin.getEmail());
        dto.put("role", "ADMIN");
        dto.put("lastSeen", admin.getLastSeen());
        return dto;
    }
}
