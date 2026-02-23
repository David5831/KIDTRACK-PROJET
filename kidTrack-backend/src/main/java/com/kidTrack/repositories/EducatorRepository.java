package com.kidTrack.repositories;

import com.kidTrack.models.Educator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducatorRepository extends JpaRepository<Educator, Integer> {
    List<Educator> findByGroupId(Integer groupId);
    
    @Query("SELECT e.group.id FROM Educator e WHERE e.id = :educatorId")
    Integer findGroupIdByEducatorId(Integer educatorId);
}
