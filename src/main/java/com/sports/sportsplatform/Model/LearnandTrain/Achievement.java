package com.sports.sportsplatform.Model.LearnandTrain;





import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate date;
    private String certificateImageUrl;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
}

