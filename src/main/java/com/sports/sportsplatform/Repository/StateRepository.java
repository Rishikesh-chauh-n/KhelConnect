package com.sports.sportsplatform.Repository;


import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findByNameContainingIgnoreCase(String name);
}


