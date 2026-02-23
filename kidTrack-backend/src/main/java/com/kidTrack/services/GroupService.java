package com.kidTrack.services;

import com.kidTrack.models.Educator;
import com.kidTrack.models.Group;
import com.kidTrack.repositories.EducatorRepository;
import com.kidTrack.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private EducatorRepository educatorRepository;
    
    public List<Map<String, Object>> getAllGroups() {
        return groupRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<Map<String, Object>> getGroupById(Integer id) {
        return groupRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public Map<String, Object> createGroup(Map<String, Object> groupData) {
        Group group = new Group();
        group.setName((String) groupData.get("name"));
        group.setDescription((String) groupData.get("description"));
        group.setAgeMin((Integer) groupData.get("ageMin"));
        group.setAgeMax((Integer) groupData.get("ageMax"));
        group.setCapacity((Integer) groupData.get("capacity"));
        
        Group savedGroup = groupRepository.save(group);
        
        // Assign educator if provided
        if (groupData.containsKey("educatorId") && groupData.get("educatorId") != null) {
            Integer educatorId = (Integer) groupData.get("educatorId");
            Educator educator = educatorRepository.findById(educatorId)
                .orElseThrow(() -> new RuntimeException("Educator not found with id: " + educatorId));
            educator.setGroup(savedGroup);
            educatorRepository.save(educator);
        }
        
        return convertToDTO(savedGroup);
    }
    
    public Map<String, Object> updateGroup(Integer id, Map<String, Object> groupData) {
        Group group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        
        group.setName((String) groupData.get("name"));
        group.setDescription((String) groupData.get("description"));
        group.setAgeMin((Integer) groupData.get("ageMin"));
        group.setAgeMax((Integer) groupData.get("ageMax"));
        group.setCapacity((Integer) groupData.get("capacity"));
        
        Group updatedGroup = groupRepository.save(group);
        
        // Update educator assignment
        // First, remove this group from any educator that had it
        List<Educator> oldEducators = educatorRepository.findByGroupId(id);
        for (Educator oldEducator : oldEducators) {
            oldEducator.setGroup(null);
            educatorRepository.save(oldEducator);
        }
        
        // Then assign new educator if provided
        if (groupData.containsKey("educatorId") && groupData.get("educatorId") != null) {
            Integer educatorId = (Integer) groupData.get("educatorId");
            Educator educator = educatorRepository.findById(educatorId)
                .orElseThrow(() -> new RuntimeException("Educator not found with id: " + educatorId));
            educator.setGroup(updatedGroup);
            educatorRepository.save(educator);
        }
        
        return convertToDTO(updatedGroup);
    }
    
    public void deleteGroup(Integer id) {
        groupRepository.deleteById(id);
    }
    
    private Map<String, Object> convertToDTO(Group group) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", group.getId());
        dto.put("name", group.getName());
        dto.put("description", group.getDescription());
        dto.put("ageMin", group.getAgeMin());
        dto.put("ageMax", group.getAgeMax());
        dto.put("capacity", group.getCapacity());
        
        // Calculate current size from children
        int currentSize = group.getChildren() != null ? group.getChildren().size() : 0;
        dto.put("currentSize", currentSize);
        
        // Get educator name and ID using repository
        String educatorName = "Non assigné";
        Integer educatorId = null;
        List<Educator> educators = educatorRepository.findByGroupId(group.getId());
        if (educators != null && !educators.isEmpty()) {
            Educator educator = educators.get(0);
            educatorName = educator.getFirstName() + " " + educator.getLastName();
            educatorId = educator.getId();
        }
        dto.put("educator", educatorName);
        dto.put("educatorId", educatorId);
        
        return dto;
    }
}
