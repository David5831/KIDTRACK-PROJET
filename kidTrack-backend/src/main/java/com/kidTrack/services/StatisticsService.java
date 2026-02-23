package com.kidTrack.services;

import com.kidTrack.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ActivitiesRepository activitiesRepository;
    
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total counts
        long totalChildren = childrenRepository.count();
        long totalParents = userRepository.findAll().stream()
            .filter(user -> user.getClass().getSimpleName().equals("Parent"))
            .count();
        long totalEducators = userRepository.findAll().stream()
            .filter(user -> user.getClass().getSimpleName().equals("Educator"))
            .count();
        long totalGroups = groupRepository.count();
        
        stats.put("totalChildren", totalChildren);
        stats.put("totalParents", totalParents);
        stats.put("totalEducators", totalEducators);
        stats.put("totalGroups", totalGroups);
        
        // Group occupancy
        List<Map<String, Object>> groupOccupancy = new ArrayList<>();
        groupRepository.findAll().forEach(group -> {
            Map<String, Object> groupStats = new HashMap<>();
            groupStats.put("name", group.getName());
            
            // Calculate current size from children list
            int current = group.getChildren() != null ? group.getChildren().size() : 0;
            int max = group.getCapacity() != null ? group.getCapacity() : 15;
            
            groupStats.put("current", current);
            groupStats.put("max", max);
            
            double percentage = max > 0 ? (double) current / max : 0.0;
            groupStats.put("percentage", percentage);
            
            groupOccupancy.add(groupStats);
        });
        stats.put("groupOccupancy", groupOccupancy);
        
        // Recent activities (mock data for now)
        List<Map<String, Object>> recentActivities = new ArrayList<>();
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("type", "Nouvelles inscriptions");
        activity1.put("count", totalChildren);
        activity1.put("trend", "up");
        recentActivities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("type", "Groupes actifs");
        activity2.put("count", totalGroups);
        activity2.put("trend", "flat");
        recentActivities.add(activity2);
        
        stats.put("recentActivities", recentActivities);
        
        // Events this month
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        
        long eventsThisMonth = eventRepository.findAll().stream()
            .filter(event -> {
                LocalDate eventDate = event.getEventDate();
                return eventDate != null && 
                       !eventDate.isBefore(startOfMonth) && 
                       !eventDate.isAfter(endOfMonth);
            })
            .count();
        stats.put("eventsThisMonth", eventsThisMonth);
        
        // Activities today
        long activitiesToday = activitiesRepository.findAll().stream()
            .filter(activity -> {
                LocalDate activityDate = activity.getActivityDate();
                return activityDate != null && activityDate.equals(now);
            })
            .count();
        stats.put("activitiesToday", activitiesToday);
        
        return stats;
    }
}
