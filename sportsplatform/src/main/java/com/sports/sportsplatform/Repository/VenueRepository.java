package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue,Long> {
}
