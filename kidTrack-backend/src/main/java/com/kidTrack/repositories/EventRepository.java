package com.kidTrack.repositories;

import com.kidTrack.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByEventDateAfter(LocalDate date);
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
