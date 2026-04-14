package com.poll.dtos;

import lombok.Data;

@Data
public class LikesDTO
{
    private Long id;

    private Long userId;

    private Long pollId;

    private String username;
}
