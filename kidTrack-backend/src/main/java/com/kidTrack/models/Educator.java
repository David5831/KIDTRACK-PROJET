package com.kidTrack.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "educators")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Educator extends Staff {
    
    @ToString.Exclude
    @JsonIgnoreProperties({"children", "educators", "meals"})
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
    
    
}
