package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByNameContainingIgnoreCase(String name);


    Optional<Game> findByName(String name);


}

