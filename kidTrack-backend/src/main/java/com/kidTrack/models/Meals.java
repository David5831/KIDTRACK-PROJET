package com.kidTrack.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meals {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "meal_date")
    private java.time.LocalDate mealDate;
    
    @ElementCollection
    @CollectionTable(name = "meal_items", joinColumns = @JoinColumn(name = "meal_id"))
    @Column(name = "item")
    private List<String> menuItems;
    
    @ElementCollection
    @CollectionTable(name = "meal_dietary_restrictions", joinColumns = @JoinColumn(name = "meal_id"))
    @Column(name = "restriction")
    private List<String> dietaryRestrictions;
    
    @ManyToMany
    @JoinTable(
        name = "meal_groups",
        joinColumns = @JoinColumn(name = "meal_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public java.time.LocalDate getMealDate() {
		return mealDate;
	}

	public void setMealDate(java.time.LocalDate mealDate) {
		this.mealDate = mealDate;
	}

	public List<String> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<String> menuItems) {
		this.menuItems = menuItems;
	}

	public List<String> getDietaryRestrictions() {
		return dietaryRestrictions;
	}

	public void setDietaryRestrictions(List<String> dietaryRestrictions) {
		this.dietaryRestrictions = dietaryRestrictions;
	}
}
