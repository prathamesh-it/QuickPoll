package com.poll.controllers.auth;

import com.poll.Entity.User;
import com.poll.Repositories.UserRepository;
import com.poll.dtos.AuthenticationRequest;
import com.poll.dtos.AuthenticationResponse;
import com.poll.dtos.SignUpRequest;
import com.poll.dtos.UserDTO;
import com.poll.services.auth.AuthService;
import com.poll.services.jwt.UserService;
import com.poll.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController  //return JSON
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController
{
    private final AuthService authService;  // // signup logic

    private final UserService userService;  // load user from DB

    private final JWTUtil jwtUtil;   // generate JWT token

    private final AuthenticationManager authenticationManager;    // verify login credentials

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signUpRequest)
    {
        try
        {
            if (authService.hasUserWithEmail(signUpRequest.getEmail()))
            {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error" , "Email is already in use"));
            }

            UserDTO createdUser = authService.createUser(signUpRequest);
            if(createdUser == null)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error" , "User Creation Failed, please try agaon later"));
            }

            UserDetails userDetails = userService.userDetailService().loadUserByUsername(createdUser.getEmail());

            String jwt = jwtUtil.generateToken(userDetails , createdUser.getId());

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setJwtToken(jwt);
            authenticationResponse.setName(createdUser.getFirstName() + " " + createdUser.getLastName());

            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error" , "An Unexpected error occurred : "+ e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest)
    {
        try
        {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken
                            (authenticationRequest.getEmail(), authenticationRequest.getPassword()));



            UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
            Optional<User>optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

            if(optionalUser.isPresent())
            {
                User user = optionalUser.get();
                String jwt = jwtUtil.generateToken(userDetails , user.getId());

                AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                authenticationResponse.setJwtToken(jwt);
                authenticationResponse.setName(user.getFirstName() + " " + user.getLastName());

                return ResponseEntity.ok(authenticationResponse);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message" , "User not found"));

        }
        //Wrong username/password → 401 UNAUTHORIZED
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Incorrect username or password"));
        }
        //Account is disabled → 403 FORBIDDEN
        catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "User account is disabled"));
        }
//        User doesn't exist → 404 NOT_FOUND
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }
        // 500 INTERNAL_SERVER_ERROR
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error" , "An Unexpected error occurred : "+ e.getMessage()));
        }

    }


}
