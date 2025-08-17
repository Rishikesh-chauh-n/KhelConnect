
package com.sports.sportsplatform.Repository;
import org.springframework.data.domain.Pageable;


import com.sports.sportsplatform.Model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    boolean existsByUserEmail(String email);  // ✅ Correct



    // Count total interactions for a given user
    long countByUserId(Long userId);

    // Get the oldest interaction IDs for a given user (paginated)
    @Query("SELECT ui.id FROM UserInteraction ui " +
            "WHERE ui.user.id = :userId " +
            "ORDER BY ui.timestamp ASC")
    List<Long> findOldestInteractionIds(@Param("userId") Long userId, Pageable pageable);

    // Delete multiple interactions by IDs
    void deleteByIdIn(List<Long> ids);


    @Query("SELECT ui.keyword, COUNT(ui) as count " +
            "FROM UserInteraction ui " +
            "WHERE ui.user.id = :userId AND ui.interactionType = 'GAME' " +
            "GROUP BY ui.keyword " +
            "ORDER BY count DESC")
    List<Object[]> findTopGamesByUser(@Param("userId") Long userId);



    @Query("SELECT ui.keyword " +
            "FROM UserInteraction ui " +
            "WHERE ui.user.id = :userId " +
            "GROUP BY ui.keyword " +
            "ORDER BY COUNT(ui.keyword) DESC")
    List<String> findPreferredGamesByUserId(@Param("userId") Long userId);
}

