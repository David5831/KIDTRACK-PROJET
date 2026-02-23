package com.kidTrack.controllers;

import com.kidTrack.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getGroupById(@PathVariable Integer id) {
        return groupService.getGroupById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createGroup(@RequestBody Map<String, Object> groupData) {
        return ResponseEntity.ok(groupService.createGroup(groupData));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateGroup(@PathVariable Integer id, @RequestBody Map<String, Object> groupData) {
        return ResponseEntity.ok(groupService.updateGroup(id, groupData));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }
}
