package com.poll.dtos;

import lombok.Data;

@Data
//What user sends to login
public class AuthenticationRequest
{
    private String email;
    private String password;
}
