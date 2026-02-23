package com.kidTrack.repositories;

import com.kidTrack.models.NapTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface NapTimeRepository extends JpaRepository<NapTime, Integer> {
    
    List<NapTime> findByChildrenId(Integer childId);
    
    List<NapTime> findByChildrenIdAndNapDate(Integer childId, LocalDate napDate);
    
    List<NapTime> findByNapDate(LocalDate napDate);
    
    List<NapTime> findByChildrenIdOrderByNapDateDesc(Integer childId);
}
