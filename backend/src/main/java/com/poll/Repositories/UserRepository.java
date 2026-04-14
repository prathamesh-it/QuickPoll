package com.poll.Repositories;

import com.poll.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long>
{
    //SELECT * FROM users WHERE email = ? LIMIT 1
    Optional<User> findFirstByEmail(String email);
}
