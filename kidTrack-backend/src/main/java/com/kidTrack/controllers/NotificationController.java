package com.kidTrack.controllers;

import com.kidTrack.models.NotificationEntity;
import com.kidTrack.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationEntity>> getUserNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
    
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationEntity>> getUnreadNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }
    
    @PostMapping
    public ResponseEntity<NotificationEntity> createNotification(@RequestBody NotificationEntity notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationEntity> markAsRead(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}
