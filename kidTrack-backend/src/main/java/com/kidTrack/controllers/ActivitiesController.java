package com.kidTrack.controllers;

import com.kidTrack.dto.ActivityDTO;
import com.kidTrack.models.Activities;
import com.kidTrack.models.Comment;
import com.kidTrack.services.ActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivitiesController {
    
    @Autowired
    private ActivitiesService activitiesService;
    
    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {
        return ResponseEntity.ok(activitiesService.getAllActivitiesDTO());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Integer id) {
        return activitiesService.getActivityByIdDTO(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/child/{childId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByChildId(@PathVariable Integer childId) {
        return ResponseEntity.ok(activitiesService.getActivitiesByChildIdDTO(childId));
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByGroupId(@PathVariable Integer groupId) {
        return ResponseEntity.ok(activitiesService.getActivitiesByGroupIdDTO(groupId));
    }
    
    @PostMapping
    public ResponseEntity<Activities> createActivity(@RequestBody Activities activity) {
        return ResponseEntity.ok(activitiesService.createActivity(activity));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Activities> updateActivity(@PathVariable Integer id, @RequestBody Activities activity) {
        return ResponseEntity.ok(activitiesService.updateActivity(id, activity));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Integer id) {
        activitiesService.deleteActivity(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getActivityComments(@PathVariable Integer id) {
        return ResponseEntity.ok(activitiesService.getActivityComments(id));
    }
    
    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(activitiesService.addComment(comment));
    }
}
