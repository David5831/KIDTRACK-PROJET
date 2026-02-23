package com.kidTrack.controllers;

import com.kidTrack.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {
    
    @Autowired
    private ContactService contactService;
    
    /**
     * Get available contacts for a parent (educators of their children's groups)
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Map<String, Object>>> getParentContacts(@PathVariable Integer parentId) {
        return ResponseEntity.ok(contactService.getEducatorsForParent(parentId));
    }
    
    /**
     * Get available contacts for an educator (parents of their group's children)
     */
    @GetMapping("/educator/{educatorId}")
    public ResponseEntity<List<Map<String, Object>>> getEducatorContacts(@PathVariable Integer educatorId) {
        return ResponseEntity.ok(contactService.getParentsForEducator(educatorId));
    }
    
    /**
     * Get all parents (for admin)
     */
    @GetMapping("/admin/parents")
    public ResponseEntity<List<Map<String, Object>>> getAllParents() {
        return ResponseEntity.ok(contactService.getAllParents());
    }
    
    /**
     * Get all educators (for admin)
     */
    @GetMapping("/admin/educators")
    public ResponseEntity<List<Map<String, Object>>> getAllEducators() {
        return ResponseEntity.ok(contactService.getAllEducators());
    }
}
