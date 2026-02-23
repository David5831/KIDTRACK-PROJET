package com.kidTrack.services;

import com.kidTrack.models.Event;
import com.kidTrack.models.Children;
import com.kidTrack.repositories.EventRepository;
import com.kidTrack.repositories.ChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventsService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }
    
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDate.now());
    }
    
    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        // Notifier tous les parents
        notificationService.notifyNewEvent(savedEvent);
        return savedEvent;
    }
    
    public Event updateEvent(Integer id, Event eventDetails) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setEventDate(eventDetails.getEventDate());
        event.setMaxParticipants(eventDetails.getMaxParticipants());
        
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
    
    public Event registerChildToEvent(Integer eventId, Integer childId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        Children child = childrenRepository.findById(childId)
            .orElseThrow(() -> new RuntimeException("Child not found with id: " + childId));
        
        // Vérifier le nombre maximum de participants
        if (event.getMaxParticipants() != null && 
            event.getParticipants().size() >= event.getMaxParticipants()) {
            throw new RuntimeException("Event is full");
        }
        
        event.getParticipants().add(child);
        return eventRepository.save(event);
    }
    
    public Event unregisterChildFromEvent(Integer eventId, Integer childId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        event.getParticipants().removeIf(child -> child.getId().equals(childId));
        return eventRepository.save(event);
    }
}
