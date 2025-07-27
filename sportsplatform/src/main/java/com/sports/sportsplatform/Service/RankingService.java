package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Medal;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserProfile;
import com.sports.sportsplatform.Repository.MedalRepository;
import com.sports.sportsplatform.Repository.UserProfileRepository;
import com.sports.sportsplatform.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final UserRepository userRepository;
    private final MedalRepository medalRepository;
    private final UserProfileRepository userProfileRepository;

    // This method runs every midnight to update rankings
    @Scheduled(cron = "0 0 0 * * *")
    public void updateUserRankings() {
        List<User> users = userRepository.findAll();
        Map<UserProfile, Integer> profileScoreMap = new HashMap<>();

        for (User user : users) {
            Optional<UserProfile> profileOpt = userProfileRepository.findByUser(user);
            if (profileOpt.isEmpty()) continue;

            UserProfile profile = profileOpt.get();
            List<Medal> medals = medalRepository.findByUserProfile(profile);

            int score = calculateScore(medals);
            profileScoreMap.put(profile, score);
        }

        // Sort profiles by score in descending order
        List<Map.Entry<UserProfile, Integer>> sortedEntries = new ArrayList<>(profileScoreMap.entrySet());
        sortedEntries.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;
        for (Map.Entry<UserProfile, Integer> entry : sortedEntries) {
            UserProfile profile = entry.getKey();
            profile.setRankScore(rank++);
            userProfileRepository.save(profile);
        }

        System.out.println("✅ Rankings updated successfully!");
    }

    private int calculateScore(List<Medal> medals) {
        int score = 0;
        for (Medal medal : medals) {
            switch (medal.getType().toLowerCase()) {
                case "gold" -> score += 3;
                case "silver" -> score += 2;
                case "bronze" -> score += 1;
            }
        }
        return score;
    }
}
