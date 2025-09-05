package com.sports.sportsplatform.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "profile")
@EqualsAndHashCode(exclude = "profile")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String role; // e.g., "USER", "ADMIN"

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSettings settings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInteraction> interactions = new ArrayList<>();


}
