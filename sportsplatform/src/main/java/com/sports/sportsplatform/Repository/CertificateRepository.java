package com.sports.sportsplatform.Repository;

import com.sports.sportsplatform.Model.Certificate;
import com.sports.sportsplatform.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByUserProfile(UserProfile userProfile);
}
