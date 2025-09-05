package com.sports.sportsplatform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Gold, Silver, Bronze
    private String level; // International, National, State

    @Lob
    private byte[] imageUrl; // Optional: rename to imageData for clarity

    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
