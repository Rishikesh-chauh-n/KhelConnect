package com.sports.sportsplatform.Model.Venues;

import com.sports.sportsplatform.Model.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VenueGameDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private Venue venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude   // avoid infinite recursion
    private Game game;

    private String startTime;     // e.g., "08:00"
    private String endTime;       // e.g., "18:00"
    private int slotsPerHour;     // e.g., 5

    private double pricePerSlot;  // 💰 e.g., 150.0 INR
}
