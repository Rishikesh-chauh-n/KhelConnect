package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    @Query("SELECT DISTINCT p.user FROM UserSettings p " +
            "JOIN p.notificationGames g " +
            "WHERE p.notificationState.id = :stateId " +
            "AND p.notificationDistrict.id = :districtId " +
            "AND g.id IN :gameIds")
    List<User> findMatchingUsersForGames(@Param("stateId") Long stateId,
                                         @Param("districtId") Long districtId,
                                         @Param("gameIds") List<Long> gameIds);



    Optional<UserSettings> findByUser(User user);
}
