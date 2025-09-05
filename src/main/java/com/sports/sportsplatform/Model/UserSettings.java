package com.sports.sportsplatform.Model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the user
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // State preference
    @ManyToOne
    @JoinColumn(name = "notification_state_id")
    private State notificationState;

    @ManyToOne
    @JoinColumn(name = "notification_district_id")
    private District notificationDistrict;

    @ManyToMany
    @JoinTable(
            name = "user_settings_notification_games",
            joinColumns = @JoinColumn(name = "settings_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> notificationGames = new HashSet<>();

}
