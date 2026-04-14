package com.poll.services.auth;

import com.poll.dtos.SignUpRequest;
import com.poll.dtos.UserDTO;

public interface AuthService
{
    UserDTO createUser(SignUpRequest signUpRequest);
    Boolean hasUserWithEmail(String email);
}
