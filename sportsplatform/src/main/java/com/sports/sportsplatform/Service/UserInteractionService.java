package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserInteraction;
import com.sports.sportsplatform.Repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    private final UserInteractionRepository interactionRepo;

    public void saveInteraction(User user, String keyword) {
        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
      // "search" or "click"
        interaction.setKeyword(keyword);
        interaction.setTimestamp(LocalDateTime.now());

        interactionRepo.save(interaction);
    }


    public boolean isFirstTimeUser(String email) {
        // Option 1: Using existsByUsername (recommended if you create this query)
        return !interactionRepo.existsByUserEmail(email);

        // OR Option 2: By checking size of list (if no existsByUsername method)
        // List<UserInteraction> interactions = userInteractionRepository.findByUsername(username);
        // return interactions.isEmpty();
    }
}
