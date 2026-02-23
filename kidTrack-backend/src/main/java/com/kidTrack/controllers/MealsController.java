package com.kidTrack.controllers;

import com.kidTrack.models.Meals;
import com.kidTrack.services.MealsService;
import com.kidTrack.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = "*")
public class MealsController {
    
    @Autowired
    private MealsService mealsService;
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<Meals>> getAllMeals() {
        return ResponseEntity.ok(mealsService.getAllMeals());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Meals> getMealById(@PathVariable Integer id) {
        return mealsService.getMealById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Meals> createMeal(@RequestBody Meals meal) {
        Meals createdMeal = mealsService.createMeal(meal);
        
        // Notifier les parents des groupes concernés
        try {
            notificationService.notifyNewMeal(createdMeal);
        } catch (Exception e) {
            // Log l'erreur mais ne pas empêcher la création du repas
            System.err.println("Erreur lors de l'envoi des notifications : " + e.getMessage());
        }
        
        return ResponseEntity.ok(createdMeal);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Meals> updateMeal(@PathVariable Integer id, @RequestBody Meals meal) {
        return ResponseEntity.ok(mealsService.updateMeal(id, meal));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Integer id) {
        mealsService.deleteMeal(id);
        return ResponseEntity.ok().build();
    }
}
