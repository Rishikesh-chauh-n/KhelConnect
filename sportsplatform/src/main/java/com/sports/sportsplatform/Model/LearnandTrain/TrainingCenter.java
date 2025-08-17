package com.sports.sportsplatform.Model.LearnandTrain;



import com.sports.sportsplatform.Model.District;
import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.State;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "training_centers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private String address;
    private String contactNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "training_center_games",
            joinColumns = @JoinColumn(name = "training_center_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> sportsOffered;

    @OneToMany(mappedBy = "trainingCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trainee> trainees;
}
