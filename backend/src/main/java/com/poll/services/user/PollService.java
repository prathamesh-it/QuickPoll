package com.poll.services.user;

import com.poll.Entity.Poll;
import com.poll.dtos.*;

import java.util.List;

public interface PollService
{
    PollDTO postPoll(PollDTO pollDTO);

    void deletePoll(Long id);

    List<PollDTO> getAllPolls();

    List<PollDTO> getMyPolls();

    LikesDTO giveLikeToPoll(Long id);

    CommentDTO postCommentOnPoll(CommentDTO commentDTO);

    VoteDTO postVoteOnPoll(VoteDTO voteDTO);

    PollDetailsDTO getPollById(Long id);
}
