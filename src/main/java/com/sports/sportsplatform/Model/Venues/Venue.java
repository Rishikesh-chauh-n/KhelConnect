package com.sports.sportsplatform.Model.Venues;

import com.sports.sportsplatform.Model.District;
import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;
import com.sports.sportsplatform.Model.Venues.BookedSlotVenue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

//public class Venue {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String location;
//    private String imagePath;
//    private String description;
//
//    @ElementCollection
//    private Set<LocalDate> unavailableDates = new HashSet<>();
//
//    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<BookedSlotVenue> bookedSlots = new ArrayList<>();
//
//    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<VenueGameDetails> gameDetailsList = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "district_id")
//    @ToString.Exclude
//    private District district;
//
//    @ElementCollection
//    private List<String> amenities = new ArrayList<>();
//}



public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String imagePath;
    private String description;

    @ElementCollection
    private Set<LocalDate> unavailableDates = new HashSet<>();

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookedSlotVenue> bookedSlots = new ArrayList<>();

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude   // prevent Lombok from recursively calling toString()
    private List<VenueGameDetails> gameDetailsList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "district_id")
    @ToString.Exclude
    private District district;

    @ElementCollection
    private List<String> amenities = new ArrayList<>();
}
