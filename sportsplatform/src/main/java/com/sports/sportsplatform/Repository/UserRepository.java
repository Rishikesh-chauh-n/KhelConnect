package com.sports.sportsplatform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sports.sportsplatform.Model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
