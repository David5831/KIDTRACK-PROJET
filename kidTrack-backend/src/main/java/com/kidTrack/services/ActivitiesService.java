package com.kidTrack.services;

import com.kidTrack.dto.ActivityDTO;
import com.kidTrack.models.Activities;
import com.kidTrack.models.Comment;
import com.kidTrack.repositories.ActivitiesRepository;
import com.kidTrack.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivitiesService {
    
    @Autowired
    private ActivitiesRepository activitiesRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    public List<Activities> getAllActivities() {
        return activitiesRepository.findAll();
    }
    
    public List<ActivityDTO> getAllActivitiesDTO() {
        return activitiesRepository.findAll().stream()
            .map(ActivityDTO::new)
            .collect(Collectors.toList());
    }
    
    public Optional<Activities> getActivityById(Integer id) {
        return activitiesRepository.findById(id);
    }
    
    public Optional<ActivityDTO> getActivityByIdDTO(Integer id) {
        return activitiesRepository.findById(id)
            .map(ActivityDTO::new);
    }
    
    public List<Activities> getActivitiesByChildId(Integer childId) {
        return activitiesRepository.findByChildrenId(childId);
    }
    
    public List<ActivityDTO> getActivitiesByChildIdDTO(Integer childId) {
        return activitiesRepository.findByChildrenId(childId).stream()
            .map(ActivityDTO::new)
            .collect(Collectors.toList());
    }
    
    public List<ActivityDTO> getActivitiesByGroupIdDTO(Integer groupId) {
        return activitiesRepository.findByChildrenGroupId(groupId).stream()
            .map(ActivityDTO::new)
            .collect(Collectors.toList());
    }
    
    public Activities createActivity(Activities activity) {
        return activitiesRepository.save(activity);
    }
    
    public Activities updateActivity(Integer id, Activities activityDetails) {
        Activities activity = activitiesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        
        activity.setPhotoImage(activityDetails.getPhotoImage());
        activity.setDescription(activityDetails.getDescription());
        activity.setChildren(activityDetails.getChildren());
        
        return activitiesRepository.save(activity);
    }
    
    public void deleteActivity(Integer id) {
        activitiesRepository.deleteById(id);
    }
    
    public List<Comment> getActivityComments(Integer activityId) {
        return commentRepository.findByActivityId(activityId);
    }
    
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
