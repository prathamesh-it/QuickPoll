package com.poll.Repositories;

import com.poll.Entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findAllByPollId(Long id);

    Optional<Likes> findByPollIdAndUserId(Long pollId, Long id);
}
