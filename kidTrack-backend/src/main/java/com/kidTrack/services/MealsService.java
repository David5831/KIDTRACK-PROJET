package com.kidTrack.services;

import com.kidTrack.models.Meals;
import com.kidTrack.repositories.MealsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MealsService {
    
    @Autowired
    private MealsRepository mealsRepository;
    
    public List<Meals> getAllMeals() {
        return mealsRepository.findAll();
    }
    
    public Optional<Meals> getMealById(Integer id) {
        return mealsRepository.findById(id);
    }
    
    public Meals createMeal(Meals meal) {
        return mealsRepository.save(meal);
    }
    
    public Meals updateMeal(Integer id, Meals mealDetails) {
        Meals meal = mealsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Meal not found with id: " + id));
        
        meal.setName(mealDetails.getName());
        meal.setDescription(mealDetails.getDescription());
        meal.setGroups(mealDetails.getGroups());
        
        return mealsRepository.save(meal);
    }
    
    public void deleteMeal(Integer id) {
        mealsRepository.deleteById(id);
    }
}
