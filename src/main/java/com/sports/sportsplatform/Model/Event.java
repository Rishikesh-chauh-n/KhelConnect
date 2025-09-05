package com.sports.sportsplatform.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location; // e.g., city or stadium name

    private LocalDate date;

     // ✅ Add this

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;


    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;



    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }


}
