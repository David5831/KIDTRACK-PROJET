package com.kidTrack.controllers;

import com.kidTrack.models.Event;
import com.kidTrack.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventsController {
    
    @Autowired
    private EventsService eventsService;
    
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventsService.getAllEvents());
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventsService.getUpcomingEvents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer id) {
        return eventsService.getEventById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventsService.createEvent(event));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Integer id, @RequestBody Event event) {
        return ResponseEntity.ok(eventsService.updateEvent(id, event));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventsService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{eventId}/register/{childId}")
    public ResponseEntity<Event> registerChild(@PathVariable Integer eventId, @PathVariable Integer childId) {
        return ResponseEntity.ok(eventsService.registerChildToEvent(eventId, childId));
    }
    
    @DeleteMapping("/{eventId}/unregister/{childId}")
    public ResponseEntity<Event> unregisterChild(@PathVariable Integer eventId, @PathVariable Integer childId) {
        return ResponseEntity.ok(eventsService.unregisterChildFromEvent(eventId, childId));
    }
}
