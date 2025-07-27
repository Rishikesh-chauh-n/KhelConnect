package com.sports.sportsplatform.Service;

import com.sports.sportsplatform.Model.User;
import com.sports.sportsplatform.Model.UserSettings;
import com.sports.sportsplatform.Repository.UserRepository;
import com.sports.sportsplatform.Repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSettings saveSettings(UserSettings settings) {
        return userSettingsRepository.save(settings);
    }



    public List<UserSettings> getAllSettings() {
        return userSettingsRepository.findAll();
    }

    public void deleteSettings(Long id) {
        userSettingsRepository.deleteById(id);
    }


    public UserSettings updatePreferences(String email, String prefer1, String prefer2, String prefer3) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        Optional<UserSettings> optional = userSettingsRepository.findByUser(user);
        if (optional.isPresent()) {
            UserSettings settings = optional.get();
            settings.setPrefer1(prefer1);
            settings.setPrefer2(prefer2);
            settings.setPrefer3(prefer3);
            return userSettingsRepository.save(settings);
        }
        return null;
    }

    public UserSettings updateNotificationGames(String email, List<String> games) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        Optional<UserSettings> optional = userSettingsRepository.findByUser(user);
        if (optional.isPresent()) {
            UserSettings settings = optional.get();
            settings.setNotificationGames(games);
            return userSettingsRepository.save(settings);
        }
        return null;
    }

    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return false; // old password doesn't match
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
