package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Venues.BookedSlotVenue;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookedSlotVenueRepository extends JpaRepository<BookedSlotVenue, Long> {

    @Query("SELECT b.venueGameDetails.id, b.timeSlot, SUM(b.bookedCount) " +
            "FROM BookedSlotVenue b " +
            "WHERE b.venue.id = :venueId AND b.date = :date " +
            "GROUP BY b.venueGameDetails.id, b.timeSlot")
    List<Object[]> findBookedCountsForVenueAndDate(@Param("venueId") Long venueId,
                                                   @Param("date") LocalDate date);


//
//    boolean existsByVenueGameDetailsIdAndDateAndTimeSlotAndStatusIn(
//            Long venueGameDetailsId,
//            LocalDate date,
//            LocalTime timeSlot,
//            List<ReservationStatus> statuses
//    );
//
//    List<BookedSlotVenue> findByStatusAndHoldExpiryBefore(
//            ReservationStatus status,
//            LocalDateTime now
//    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BookedSlotVenue b WHERE b.venue.id = :venueId AND b.venueGameDetails.id = :gameId AND b.date = :date AND b.timeSlot = :timeSlot")
    Optional<BookedSlotVenue> findForUpdate(@Param("venueId") Long venueId,
                                            @Param("gameId") Long gameId,
                                            @Param("date") LocalDate date,
                                            @Param("timeSlot") LocalTime timeSlot);

}
