package com.kidTrack.services;

import com.kidTrack.models.NapTime;
import com.kidTrack.models.Children;
import com.kidTrack.repositories.NapTimeRepository;
import com.kidTrack.repositories.ChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class NapTimeService {
    
    @Autowired
    private NapTimeRepository napTimeRepository;
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    // Convert NapTime entity to DTO
    private Map<String, Object> convertToDTO(NapTime napTime) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", napTime.getId());
        dto.put("napDate", napTime.getNapDate().toString());
        dto.put("startTime", napTime.getStartTime().toString());
        dto.put("endTime", napTime.getEndTime() != null ? napTime.getEndTime().toString() : null);
        dto.put("notes", napTime.getNotes());
        
        // Calculate duration if end time exists
        if (napTime.getEndTime() != null) {
            Duration duration = Duration.between(napTime.getStartTime(), napTime.getEndTime());
            long minutes = duration.toMinutes();
            dto.put("durationMinutes", minutes);
            dto.put("durationFormatted", String.format("%dh%02dm", minutes / 60, minutes % 60));
        } else {
            dto.put("durationMinutes", null);
            dto.put("durationFormatted", "En cours");
        }
        
        // Get child info
        if (napTime.getChildren() != null) {
            dto.put("childId", napTime.getChildren().getId());
            dto.put("childName", napTime.getChildren().getFirstName() + " " + napTime.getChildren().getLastName());
        }
        
        return dto;
    }
    
    public List<Map<String, Object>> getAllNapTimes() {
        return napTimeRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<Map<String, Object>> getNapTimeById(Integer id) {
        return napTimeRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    public List<Map<String, Object>> getNapTimesByChildId(Integer childId) {
        return napTimeRepository.findByChildrenIdOrderByNapDateDesc(childId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getNapTimesByDate(LocalDate date) {
        return napTimeRepository.findByNapDate(date).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getNapTimesByChildAndDate(Integer childId, LocalDate date) {
        return napTimeRepository.findByChildrenIdAndNapDate(childId, date).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Methods that return NapTime entities directly
    public List<NapTime> getAllNapTimesEntities() {
        return napTimeRepository.findAll();
    }
    
    public Optional<NapTime> getNapTimeByIdEntity(Integer id) {
        return napTimeRepository.findById(id);
    }
    
    public NapTime createNapTime(NapTime napTime) {
        return napTimeRepository.save(napTime);
    }
    
    public NapTime updateNapTime(Integer id, NapTime napTimeDetails) {
        NapTime napTime = napTimeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("NapTime not found with id: " + id));
        
        napTime.setNapDate(napTimeDetails.getNapDate());
        napTime.setStartTime(napTimeDetails.getStartTime());
        napTime.setEndTime(napTimeDetails.getEndTime());
        napTime.setNotes(napTimeDetails.getNotes());
        
        if (napTimeDetails.getChildren() != null) {
            napTime.setChildren(napTimeDetails.getChildren());
        }
        
        return napTimeRepository.save(napTime);
    }
    
    public void deleteNapTime(Integer id) {
        napTimeRepository.deleteById(id);
    }
}
