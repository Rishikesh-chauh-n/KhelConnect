package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.Game;
import com.sports.sportsplatform.Model.Medal;
import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserProfile;
import com.sports.sportsplatform.Repository.CertificateRepository;
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
    private final UserProfileRepository userProfileRepository;
    private final MedalRepository medalRepository;

    // Run daily at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void updateUserRankings() {
        List<User> users = userRepository.findAll();


        Map<Game, List<UserProfile>> gameProfilesMap = new HashMap<>();

        for (User user : users) {
            UserProfile profile = user.getProfile();
            if (profile == null || profile.getSelectedGame() == null) continue;

            gameProfilesMap
                    .computeIfAbsent(profile.getSelectedGame(), g -> new ArrayList<>())
                    .add(profile);
        }


        for (Map.Entry<Game, List<UserProfile>> entry : gameProfilesMap.entrySet()) {
            Game game = entry.getKey();
            List<UserProfile> profiles = entry.getValue();


            Map<UserProfile, Integer> profileScoreMap = new HashMap<>();
            for (UserProfile profile : profiles) {
                int score = calculateScore(profile);
                profileScoreMap.put(profile, score);
            }

            // Sort by score descending
            List<Map.Entry<UserProfile, Integer>> sortedEntries = new ArrayList<>(profileScoreMap.entrySet());
            sortedEntries.sort((a, b) -> b.getValue() - a.getValue());

            // Assign rank
            int rank = 1;
            for (Map.Entry<UserProfile, Integer> ranked : sortedEntries) {
                UserProfile profile = ranked.getKey();
                profile.setRankScore(rank++);
                userProfileRepository.save(profile);
            }

            System.out.println("✅ Rankings updated for game=" + game.getName());
        }
    }

    private int calculateScore(UserProfile profile) {
        int score = 0;

        // Only verified medals
        List<Medal> medals = medalRepository.findByUserProfileAndVerifiedTrue(profile);
        for (Medal medal : medals) {
            switch (medal.getType().toLowerCase()) {
                case "gold" -> score += 3;
                case "silver" -> score += 2;
                case "bronze" -> score += 1;
            }

            // Weighted by level
            if ("international".equalsIgnoreCase(medal.getLevel())) score += 3;
            else if ("national".equalsIgnoreCase(medal.getLevel())) score += 2;
            else if ("state".equalsIgnoreCase(medal.getLevel())) score += 1;
        }

        return score;
    }
}


//@Service
//@RequiredArgsConstructor
//public class RankingService {
//
//    private final UserRepository userRepository;
//    private final MedalRepository medalRepository;
//    private final UserProfileRepository userProfileRepository;
//
//    // This method runs every midnight to update rankings
//    @Scheduled(cron = "0 0 0 * * *")
//    public void updateUserRankings() {
//        List<User> users = userRepository.findAll();
//        Map<UserProfile, Integer> profileScoreMap = new HashMap<>();
//
//        for (User user : users) {
//            Optional<UserProfile> profileOpt = userProfileRepository.findByUser(user);
//            if (profileOpt.isEmpty()) continue;
//
//            UserProfile profile = profileOpt.get();
//            List<Medal> medals = medalRepository.findByUserProfile(profile);
//
//            int score = calculateScore(medals);
//            profileScoreMap.put(profile, score);
//        }
//
//        // Sort profiles by score in descending order
//        List<Map.Entry<UserProfile, Integer>> sortedEntries = new ArrayList<>(profileScoreMap.entrySet());
//        sortedEntries.sort((a, b) -> b.getValue() - a.getValue());
//
//        int rank = 1;
//        for (Map.Entry<UserProfile, Integer> entry : sortedEntries) {
//            UserProfile profile = entry.getKey();
//            profile.setRankScore(rank++);
//            userProfileRepository.save(profile);
//        }
//
//        System.out.println("✅ Rankings updated successfully!");
//    }
//
//    private int calculateScore(List<Medal> medals) {
//        int score = 0;
//        for (Medal medal : medals) {
//            switch (medal.getType().toLowerCase()) {
//                case "gold" -> score += 3;
//                case "silver" -> score += 2;
//                case "bronze" -> score += 1;
//            }
//        }
//        return score;
//    }
//}



//@Service
//@RequiredArgsConstructor
//public class RankingService {
//
//    private final UserRepository userRepository;
//    private final UserProfileRepository userProfileRepository;
//    private final MedalRepository medalRepository;
//    private final CertificateRepository certificateRepository;
//
//    // Runs every midnight
//    @Scheduled(cron = "0 0 0 * * *")
//    public void updateUserRankings() {
//        List<User> users = userRepository.findAll();
//
//        // Group profiles by their selectedGame
//        Map<Long, List<UserProfile>> gameProfilesMap = new HashMap<>();
//
//        for (User user : users) {
//            UserProfile profile = user.getProfile();
//            if (profile == null || profile.getSelectedGame() == null) continue;
//
//            gameProfilesMap
//                    .computeIfAbsent(profile.getSelectedGame(), g -> new ArrayList<>())
//                    .add(profile);
//        }
//
//        // For each game, rank the profiles
//        for (Map.Entry<Long, List<UserProfile>> entry : gameProfilesMap.entrySet()) {
//            Long gameId = entry.getKey();
//            List<UserProfile> profiles = entry.getValue();
//
//            // Compute score for each profile
//            Map<UserProfile, Integer> profileScoreMap = new HashMap<>();
//            for (UserProfile profile : profiles) {
//                int score = calculateScore(profile, gameId);
//                profileScoreMap.put(profile, score);
//            }
//
//            // Sort by score (descending)
//            List<Map.Entry<UserProfile, Integer>> sortedEntries = new ArrayList<>(profileScoreMap.entrySet());
//            sortedEntries.sort((a, b) -> b.getValue() - a.getValue());
//
//            // Assign rank within this game group
//            int rank = 1;
//            for (Map.Entry<UserProfile, Integer> ranked : sortedEntries) {
//                UserProfile profile = ranked.getKey();
//                profile.setRankScore(rank++);
//                userProfileRepository.save(profile);
//            }
//
//            System.out.println("✅ Rankings updated for gameId=" + gameId);
//        }
//    }
//
//    private int calculateScore(UserProfile profile, Long gameId) {
//        int score = 0;
//
//        // Consider only verified medals belonging to this profile
//        List<Medal> medals = medalRepository.findByUserProfileAndVerifiedTrue(profile);
//        for (Medal medal : medals) {
//            // (If Medal also had game info, we’d filter here — but since game is only in profile, we skip that check)
//            switch (medal.getType().toLowerCase()) {
//                case "gold" -> score += 3;
//                case "silver" -> score += 2;
//                case "bronze" -> score += 1;
//            }
//
//            // Bonus: higher level medals can be weighted
//            if ("international".equalsIgnoreCase(medal.getLevel())) score += 3;
//            else if ("national".equalsIgnoreCase(medal.getLevel())) score += 2;
//            else if ("state".equalsIgnoreCase(medal.getLevel())) score += 1;
//        }
//
////        // Add certificates (if you want, similar repo method required)
////        List<Certificate> certificates = certificateRepository.findByUserProfileAndVerifiedTrue(profile);
////        for (Certificate cert : certificates) {
////            score += 5; // example weight per verified certificate
////        }
//
//        return score;
//    }
//}





