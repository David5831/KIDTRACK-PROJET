package com.kidTrack.controllers;

import com.kidTrack.models.Children;
import com.kidTrack.models.Allergy;
import com.kidTrack.services.ChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildrenController {
    
    @Autowired
    private ChildrenService childrenService;
    
    @GetMapping
    public ResponseEntity<List<Children>> getAllChildren() {
        return ResponseEntity.ok(childrenService.getAllChildrenEntities());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Children> getChildById(@PathVariable Integer id) {
        return childrenService.getChildByIdEntity(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Children>> getChildrenByParentId(@PathVariable Integer parentId) {
        return ResponseEntity.ok(childrenService.getChildrenByParentIdEntities(parentId));
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Children>> getChildrenByGroupId(@PathVariable Integer groupId) {
        return ResponseEntity.ok(childrenService.getChildrenByGroupIdEntities(groupId));
    }
    
    @PostMapping
    public ResponseEntity<Children> createChild(@RequestBody Children child) {
        return ResponseEntity.ok(childrenService.createChild(child));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Children> updateChild(@PathVariable Integer id, @RequestBody Children child) {
        return ResponseEntity.ok(childrenService.updateChild(id, child));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Integer id) {
        childrenService.deleteChild(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/allergies")
    public ResponseEntity<List<Allergy>> getChildAllergies(@PathVariable Integer id) {
        return ResponseEntity.ok(childrenService.getChildAllergies(id));
    }
    
    @PostMapping("/{id}/allergies")
    public ResponseEntity<Allergy> addChildAllergy(@PathVariable Integer id, @RequestBody Allergy allergy) {
        return ResponseEntity.ok(childrenService.addChildAllergy(id, allergy));
    }
}
