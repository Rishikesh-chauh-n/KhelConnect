package com.sports.sportsplatform.Model;



import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.Venues.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @ToString.Exclude
    private State state;

//    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
//    private List<Event> events = new ArrayList<>();


    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Venue> venues = new ArrayList<>();



    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingCenter> trainingCenters; // Optional
}
