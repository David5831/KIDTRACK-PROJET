package com.kidTrack.controllers;

import com.kidTrack.models.NapTime;
import com.kidTrack.services.NapTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/naptimes")
@CrossOrigin(origins = "*")
public class NapTimeController {
    
    @Autowired
    private NapTimeService napTimeService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNapTimes() {
        return ResponseEntity.ok(napTimeService.getAllNapTimes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNapTimeById(@PathVariable Integer id) {
        return napTimeService.getNapTimeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/child/{childId}")
    public ResponseEntity<List<Map<String, Object>>> getNapTimesByChildId(@PathVariable Integer childId) {
        return ResponseEntity.ok(napTimeService.getNapTimesByChildId(childId));
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Map<String, Object>>> getNapTimesByDate(@PathVariable String date) {
        LocalDate napDate = LocalDate.parse(date);
        return ResponseEntity.ok(napTimeService.getNapTimesByDate(napDate));
    }
    
    @GetMapping("/child/{childId}/date/{date}")
    public ResponseEntity<List<Map<String, Object>>> getNapTimesByChildAndDate(
            @PathVariable Integer childId,
            @PathVariable String date) {
        LocalDate napDate = LocalDate.parse(date);
        return ResponseEntity.ok(napTimeService.getNapTimesByChildAndDate(childId, napDate));
    }
    
    @PostMapping
    public ResponseEntity<NapTime> createNapTime(@RequestBody NapTime napTime) {
        return ResponseEntity.ok(napTimeService.createNapTime(napTime));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NapTime> updateNapTime(@PathVariable Integer id, @RequestBody NapTime napTime) {
        return ResponseEntity.ok(napTimeService.updateNapTime(id, napTime));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNapTime(@PathVariable Integer id) {
        napTimeService.deleteNapTime(id);
        return ResponseEntity.ok().build();
    }
}
