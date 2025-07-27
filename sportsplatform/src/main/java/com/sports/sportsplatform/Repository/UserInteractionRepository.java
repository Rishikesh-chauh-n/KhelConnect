
package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    boolean existsByUserEmail(String email);  // ✅ Correct


}
