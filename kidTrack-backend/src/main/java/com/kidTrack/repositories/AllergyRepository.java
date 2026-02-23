package com.kidTrack.repositories;

import com.kidTrack.models.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Integer> {
    List<Allergy> findByChildrenId(Integer childrenId);
}
