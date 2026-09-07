package com.example.courseenrollmentsystem.Repository;

import com.example.courseenrollmentsystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByusername(String username);

    User findByusername(String username);
}
