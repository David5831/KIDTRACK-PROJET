package com.kidTrack.services;

import com.kidTrack.models.NotificationEntity;
import com.kidTrack.models.Event;
import com.kidTrack.models.User;
import com.kidTrack.models.Meals;
import com.kidTrack.models.Group;
import com.kidTrack.models.Children;
import com.kidTrack.models.Parent;
import com.kidTrack.repositories.NotificationRepository;
import com.kidTrack.repositories.UserRepository;
import com.kidTrack.repositories.ChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ChildrenRepository childrenRepository;
    
    public List<NotificationEntity> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<NotificationEntity> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }
    
    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }
    
    public NotificationEntity markAsRead(Integer id) {
        NotificationEntity notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
    
    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }
    
    public void notifyNewEvent(Event event) {
        // Notifier tous les parents
        List<User> users = userRepository.findAll();
        for (User user : users) {
            NotificationEntity notification = new NotificationEntity();
            notification.setTitle("Nouvel événement : " + event.getTitle());
            notification.setBody(event.getDescription());
            notification.setUser(user);
            notification.setIsRead(false);
            notificationRepository.save(notification);
        }
    }
    
    public void notifyNewActivity(Integer userId, String childName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        NotificationEntity notification = new NotificationEntity();
        notification.setTitle("Nouvelle activité pour " + childName);
        notification.setBody("Une nouvelle activité a été ajoutée pour votre enfant");
        notification.setUser(user);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }
    
    public void notifyNewMeal(Meals meal) {
        // Récupérer tous les parents des enfants des groupes concernés
        Set<Parent> parents = new HashSet<>();
        
        if (meal.getGroups() != null) {
            for (Group group : meal.getGroups()) {
                // Récupérer tous les enfants du groupe
                List<Children> children = childrenRepository.findByGroupId(group.getId());
                
                // Récupérer les parents de ces enfants
                for (Children child : children) {
                    if (child.getParent() != null) {
                        parents.add(child.getParent());
                    }
                }
            }
        }
        
        // Créer les notifications pour chaque parent unique
        String mealInfo = meal.getName();
        if (meal.getMealDate() != null) {
            mealInfo += " - " + meal.getMealDate().toString();
        }
        
        for (Parent parent : parents) {
            NotificationEntity notification = new NotificationEntity();
            notification.setTitle("Nouveau repas : " + meal.getName());
            
            String body = "Un nouveau repas a été planifié";
            if (meal.getDescription() != null && !meal.getDescription().isEmpty()) {
                body += " : " + meal.getDescription();
            }
            if (meal.getMenuItems() != null && !meal.getMenuItems().isEmpty()) {
                body += "\nMenu : " + String.join(", ", meal.getMenuItems());
            }
            if (meal.getDietaryRestrictions() != null && !meal.getDietaryRestrictions().isEmpty()) {
                body += "\nRestrictions : " + String.join(", ", meal.getDietaryRestrictions());
            }
            
            notification.setBody(body);
            notification.setUser(parent);
            notification.setIsRead(false);
            notificationRepository.save(notification);
        }
    }
}
