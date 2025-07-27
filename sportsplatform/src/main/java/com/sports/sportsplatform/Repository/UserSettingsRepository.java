package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {


    Optional<UserSettings> findByUser(User user);
}
