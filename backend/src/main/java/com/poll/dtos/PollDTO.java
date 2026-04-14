package com.poll.dtos;

import com.poll.Entity.Options;
import com.poll.Entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PollDTO
{
    private Long id;

    private String question;

    private Date postedDate;

    private Date expiredAt;

    private List<String> options; // ✅ for INPUT (creating poll)

    private Integer totalVoteCount = 0;

    private boolean isExpired = false;

    private Long userId;

    private String username;

    private List<OptionsDTO> optionsDTOS; // ✅ for OUTPUT (returning poll)

    private boolean voted;

    private boolean isLiked;
}
