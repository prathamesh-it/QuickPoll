package com.poll.configs;

import com.poll.services.jwt.UserService;
import com.poll.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor                       //Runs only one per Request
public class JWTAuthenticationFilter extends OncePerRequestFilter
{
    private final JWTUtil jwtUtil;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull  HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(org.apache.commons.lang3.StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,"Bearer "))
        {
            filterChain.doFilter(request,response); //if token not then skip
            return;
        }
        jwt = authHeader.substring(7);  //"Bearer" sodun actual token gheto
        userEmail = jwtUtil.extractUsername(jwt); //extract email from token
        if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) //if email is not empty and user is not authenticated
        {
            UserDetails userDetails = userService.userDetailService().loadUserByUsername(userEmail); //load user details from database
            if(jwtUtil.isTokenValid(jwt,userDetails)) //if token is valid then authenticate user
            {
               SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities()); //create auth token with user details and authorities
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //set details of request
                context.setAuthentication(authToken); //set authentication in context
                SecurityContextHolder.setContext(context); //set context in security context holder
            }

        }
        filterChain.doFilter(request,response); //continue with the filter chain
    }
}

//JWTFilter = “request येण्याआधी check करणारा guard”
//JWTAuthenticationFilter काय करतो?
//
//        👉 प्रत्येक request साठी:
//
//Header मधून token काढतो
//Token verify करतो
//User identify करतो
//
//Spring Security ला सांगतो:

//        “हा valid user आहे”
