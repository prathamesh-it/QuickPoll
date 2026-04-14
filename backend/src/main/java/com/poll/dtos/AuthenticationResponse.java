package com.poll.dtos;

import lombok.Data;

@Data
//What server sends back to user after successful login
public class AuthenticationResponse
{
    private String jwtToken;
    private String name;
}

//User sends email + password
//        ↓
//Server verifies credentials
//        ↓
//Server generates JWT token
//        ↓
//Returns name + token to frontend
//        ↓
//Frontend stores token in localStorage
//        ↓
//Every future request includes token in header
