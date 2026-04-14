package com.poll.utils;

import com.poll.Entity.User;
import com.poll.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JWTUtil
{
    private final UserRepository userRepository;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("413F4428472B46250655368566D5970337336763779687278");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token)
    {
        return  Jwts.parser()  //to read jwt
                .verifyWith((SecretKey) getSigningKey())//to verify token use secret key
                .build()  //make final parser ready
                .parseSignedClaims(token)  //token decode + signature verify
                .getPayload();   //get payload here email and role like
    }

//    “JWT token मधून तुला हवं ते specific data काढणं” and can return any datatype
    //जेव्हा user request पाठवतो तेव्हा त्याची ओळख verify करायला
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    date देतो
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    isTokenExpired() = decision देतो
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails , Long userId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", userDetails.getUsername());
        claims.put("userId", userId);

        return generateToken(claims, userDetails);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null &&authentication.isAuthenticated()){
            User user = (User)authentication.getPrincipal();
            Optional<User> optionalUser= userRepository.findById(user.getId());
            return optionalUser.orElse(null);
        }
        return null;
    }
}
