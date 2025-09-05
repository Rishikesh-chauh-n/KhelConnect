package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Venues.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue,Long> {


    @Query("SELECT v FROM Venue v")
    List<Venue> findAllVenues(); // Fetch all venues, and then shuffle in service
// top based on number of bookings

        @Query("SELECT v FROM Venue v " +
                "LEFT JOIN FETCH v.gameDetailsList g " +
                "LEFT JOIN FETCH g.game " +
                "WHERE v.id = :id")
        Optional<Venue> findByIdWithGames(@Param("id") Long id);




}
