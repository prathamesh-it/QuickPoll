package com.poll.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService
{
    //Used by Spring Security for authentication
    UserDetailsService userDetailService();

}
