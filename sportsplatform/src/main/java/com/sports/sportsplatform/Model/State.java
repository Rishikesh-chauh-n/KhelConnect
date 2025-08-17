//package com.sports.sportsplatform.Model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class State {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    private String imageUrl; // for showing state image on frontend
//
//    @OneToMany(mappedBy = "state")
//    private List<Event> events;
//
//
//    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
//    private List<Venue> venues;
//}

package com.sports.sportsplatform.Model;
import com.sports.sportsplatform.Model.LearnandTrain.TrainingCenter;
import com.sports.sportsplatform.Model.Venues.Venue;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<District> districts;

    @OneToMany(mappedBy = "state")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Event> events;

    @OneToMany(mappedBy = "state")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TrainingCenter> trainingCenters;
}
