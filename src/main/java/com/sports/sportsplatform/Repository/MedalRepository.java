package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Medal;
import com.sports.sportsplatform.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedalRepository extends JpaRepository<Medal, Long> {

    List<Medal> findByUserProfile(UserProfile userProfile);

    List<Medal> findByUserProfileAndVerifiedTrue(UserProfile profile);


}
