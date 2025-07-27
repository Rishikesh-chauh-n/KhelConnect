package com.sports.sportsplatform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl; // for showing state image on frontend

    @OneToMany(mappedBy = "state")
    private List<Event> events;


    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<Venue> venues;
}
