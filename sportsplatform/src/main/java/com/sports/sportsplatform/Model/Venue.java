package com.sports.sportsplatform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private String imagePath;

    private double price;

    private String description;

    // ✅ For marking closed dates (e.g., maintenance/holidays)
    @ElementCollection
    private Set<LocalDate> unavailableDates = new HashSet<>();

    // ✅ For actual bookings (date + time slots)
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<BookedSlotVenue> bookedSlots = new ArrayList<>();

    // ✅ Each venue can host multiple types of games (football, cricket, etc.)
    @ManyToMany
    @JoinTable(
            name = "venue_games",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> games = new ArrayList<>();



    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
