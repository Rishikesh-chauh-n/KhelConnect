package com.sports.sportsplatform.Model;

import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.Venues.Venue;
import com.sports.sportsplatform.Model.Venues.VenueGameDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @OneToMany(mappedBy = "game")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Event> events;


    @OneToMany(mappedBy = "game")
    @ToString.Exclude   // prevent recursion
    private List<VenueGameDetails> venueGameDetailsList = new ArrayList<>();


    @ManyToMany(mappedBy = "sportsOffered")
    private List<TrainingCenter> trainingCenters;


}
