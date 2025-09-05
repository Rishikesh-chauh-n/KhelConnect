package com.sports.sportsplatform.Repository;


import com.sports.sportsplatform.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sports.sportsplatform.Model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser(User user);

    // You can add custom query methods here if needed later
}

