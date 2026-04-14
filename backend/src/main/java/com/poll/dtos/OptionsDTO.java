package com.poll.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OptionsDTO
{
    private Long id;

    private String title;

    private Long pollId;

    private Integer voteCount =0;

    private boolean userVotedThisOption;

    private List<VoteDTO> voteDTOS;
}
