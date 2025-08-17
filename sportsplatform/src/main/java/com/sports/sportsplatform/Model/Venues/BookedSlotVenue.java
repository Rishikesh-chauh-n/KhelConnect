package com.sports.sportsplatform.Model.Venues;

import com.sports.sportsplatform.Model.Booking.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedSlotVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @ToString.Exclude  // ❌ exclude venue to prevent infinite loop
    private Venue venue;

    @ManyToOne
    private VenueGameDetails venueGameDetails;

    private LocalDate date;
    private LocalTime timeSlot;
    private int bookedCount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // HOLD, CONFIRMED, CANCELLED

    private Long userId;

    @Version
    private Long version;
}

