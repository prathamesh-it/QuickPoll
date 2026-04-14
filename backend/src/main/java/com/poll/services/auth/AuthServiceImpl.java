package com.poll.services.auth;

import com.poll.Entity.User;
import com.poll.Repositories.UserRepository;
import com.poll.dtos.SignUpRequest;
import com.poll.dtos.UserDTO;
import com.poll.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUserRole(UserRole.USER);  //Every new signup automatically gets USER role — never ADMIN.
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        User createdUser = userRepository.save(user);
        return createdUser.getUserDTO();  //BCZ WE DONT WANT TO SEND PASSWORD
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
