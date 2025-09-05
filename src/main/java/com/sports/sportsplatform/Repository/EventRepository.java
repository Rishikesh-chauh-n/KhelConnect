package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findByGameId(Long gameId);
    List<Event> findByLocationContainingIgnoreCase(String location);


    @Query("SELECT e.game.id FROM Event e GROUP BY e.game.id ORDER BY COUNT(e) DESC")
    List<Long> findTopGameIdsByEventCount();

    Optional<Event> findFirstByGameIdOrderByDateAsc(Long gameId);


    List<Event> findByGameName(String gameName);



}
