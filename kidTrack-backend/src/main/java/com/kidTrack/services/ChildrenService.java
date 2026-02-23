package com.kidTrack.services;

import com.kidTrack.models.*;
import com.kidTrack.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChildrenService {
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    @Autowired
    private AllergyRepository allergyRepository;
    
    // Convert Children entity to DTO
    private Map<String, Object> convertToDTO(Children child) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", child.getId());
        dto.put("firstName", child.getFirstName());
        dto.put("lastName", child.getLastName());
        dto.put("dateOfBirth", child.getDateOfBirth().toString());
        dto.put("sexe", child.getSexe());
        dto.put("profileImage", child.getProfileImage());
        dto.put("medicalHistory", child.getMedicalHistory());
        
        // Get parent name
        String parentName = "Non assigné";
        if (child.getParent() != null) {
            parentName = child.getParent().getFirstName() + " " + child.getParent().getLastName();
        }
        dto.put("parentName", parentName);
        dto.put("parentId", child.getParent() != null ? child.getParent().getId() : null);
        
        // Get group name
        String groupName = "Non assigné";
        Integer groupId = null;
        if (child.getGroup() != null) {
            groupName = child.getGroup().getName();
            groupId = child.getGroup().getId();
        }
        dto.put("groupName", groupName);
        dto.put("groupId", groupId);
        
        // Get allergies
        List<String> allergies = child.getAllergies() != null 
            ? child.getAllergies().stream()
                .map(Allergy::getName)
                .collect(Collectors.toList())
            : List.of();
        dto.put("allergies", allergies);
        
        return dto;
    }
    
    public List<Map<String, Object>> getAllChildren() {
        return childrenRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<Map<String, Object>> getChildById(Integer id) {
        return childrenRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public List<Map<String, Object>> getChildrenByParentId(Integer parentId) {
        return childrenRepository.findByParentId(parentId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getChildrenByGroupId(Integer groupId) {
        return childrenRepository.findByGroupId(groupId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Methods that return Children entities directly
    public List<Children> getAllChildrenEntities() {
        return childrenRepository.findAll();
    }
    
    public Optional<Children> getChildByIdEntity(Integer id) {
        return childrenRepository.findById(id);
    }
    
    public List<Children> getChildrenByParentIdEntities(Integer parentId) {
        return childrenRepository.findByParentId(parentId);
    }
    
    public List<Children> getChildrenByGroupIdEntities(Integer groupId) {
        return childrenRepository.findByGroupId(groupId);
    }
    
    public Children createChild(Children child) {
        return childrenRepository.save(child);
    }
    
    public Children updateChild(Integer id, Children childDetails) {
        Children child = childrenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Child not found with id: " + id));
        
        child.setFirstName(childDetails.getFirstName());
        child.setLastName(childDetails.getLastName());
        child.setDateOfBirth(childDetails.getDateOfBirth());
        child.setSexe(childDetails.getSexe());
        child.setProfileImage(childDetails.getProfileImage());
        child.setMedicalHistory(childDetails.getMedicalHistory());
        child.setGroup(childDetails.getGroup());
        
        return childrenRepository.save(child);
    }
    
    public void deleteChild(Integer id) {
        childrenRepository.deleteById(id);
    }
    
    public List<Allergy> getChildAllergies(Integer childId) {
        return allergyRepository.findByChildrenId(childId);
    }
    
    public Allergy addChildAllergy(Integer childId, Allergy allergy) {
        Children child = childrenRepository.findById(childId)
            .orElseThrow(() -> new RuntimeException("Child not found with id: " + childId));
        allergy.setChildren(child);
        return allergyRepository.save(allergy);
    }
}
