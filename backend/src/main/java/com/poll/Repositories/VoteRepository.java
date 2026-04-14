package com.poll.Repositories;

import com.poll.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long>{

    boolean existsByPollIdAndUserId(Long pollId , Long userId);

    boolean existsByPollIdAndUserIdAndOptionsId(Long pollId , Long userId , Long optionId);
}
