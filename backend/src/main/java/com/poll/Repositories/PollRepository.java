package com.poll.Repositories;

import com.poll.Entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>
{
    List<Poll> findAllByUserId(Long id);
//    findAllByUserId(1L)
//        ↓
//    Spring generates:
//    SELECT * FROM polls WHERE user_id = 1
//        ↓
//    Returns List<Poll> → all polls created by that user
}
