package com.kidTrack.controllers;

import com.kidTrack.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}
