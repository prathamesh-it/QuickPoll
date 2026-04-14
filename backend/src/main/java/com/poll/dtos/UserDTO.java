package com.poll.dtos;

import com.poll.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO
{
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole userRole;
}
