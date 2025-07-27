package com.sports.sportsplatform.Service;




import com.sports.sportsplatform.Model.Medal;

import java.util.List;

public interface MedalService {
    List<Medal> getAllMedals();
    Medal getMedalById(Long id);
    void saveMedal(Medal medal);
    void deleteMedal(Long id);
     void updateMedalVerification(Long medalId, boolean verified);

}
