package com.sports.sportsplatform.Model.LearnandTrain;




import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "trainees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;
    private String sport;
    private String imageUrl;
    private String description;

    @ManyToOne
    @JoinColumn(name = "training_center_id")
    private TrainingCenter trainingCenter;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Achievement> achievements;
}

