package com.poll.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class VoteDTO
{
    private Long id;

    private Long optionId;

    private Long pollId;

    private Long postedBy; // who voted (user's id)

}
