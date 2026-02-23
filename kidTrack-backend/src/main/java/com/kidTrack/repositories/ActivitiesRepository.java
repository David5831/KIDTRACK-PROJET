package com.kidTrack.repositories;

import com.kidTrack.models.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Integer> {
    List<Activities> findByChildrenId(Integer childrenId);
    
    @Query("SELECT a FROM Activities a WHERE a.children.group.id = :groupId")
    List<Activities> findByChildrenGroupId(@Param("groupId") Integer groupId);
}
