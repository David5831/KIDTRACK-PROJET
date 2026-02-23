package com.kidTrack.dto;

import com.kidTrack.models.Activities;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ActivityDTO {
    private Integer id;
    private String photoImage;
    private String description;
    private LocalDate activityDate;
    private Map<String, Object> children;

    public ActivityDTO(Activities activity) {
        this.id = activity.getId();
        this.photoImage = activity.getPhotoImage();
        this.description = activity.getDescription();
        this.activityDate = activity.getActivityDate();
        
        if (activity.getChildren() != null) {
            this.children = new HashMap<>();
            this.children.put("id", activity.getChildren().getId());
            this.children.put("firstName", activity.getChildren().getFirstName());
            this.children.put("lastName", activity.getChildren().getLastName());
        }
    }
}
