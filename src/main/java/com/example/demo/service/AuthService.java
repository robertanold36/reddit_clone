package com.example.demo.service;


import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.Person;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;


    public void signup(RegisterRequest registerRequest){

            Person person=new Person();
            person.setUsername(registerRequest.getUsername());
            person.setEmail(registerRequest.getEmail());
            person.setEnabled(false);
            person.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            person.setCreated(Instant.now());

            personRepository.save(person);

            String token=generateVerificationToken(person);
            mailService.sendMail(
                    new NotificationEmail(
                            "Please activate the email",person.getEmail(),
                            "Please click the link " +
                            "below to activate your email :" +
                            "http://localhost:8081/api/auth/account/verification/"+token
                    )
            );
        }

    public String generateVerificationToken(Person person){
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setPerson(person);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication=authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtProvider.generateToken(authentication);

        RefreshToken refreshToken=refreshTokenService.generateRefreshToken();

        return AuthenticationResponse
                .builder()
                .authenticationToken(token)
                .expireAt(Instant.now().plusMillis(jwtConfig.getExpirationTime()))
                .username(loginRequest.getUsername())
                .refreshToken(refreshToken.getToken()).build();

    }


    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("token is invalid"));

        Person person=verificationToken.getPerson();
        person.setEnabled(true);
        personRepository.save(person);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenFromUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .username(refreshTokenRequest.getUsername())
                .authenticationToken(token)
                .expireAt(Instant.now().plusMillis(jwtConfig.getExpirationTime()))
                .build();

    }

    @Transactional(readOnly = true)
    public Person getCurrentUser(){
        User principal =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return personRepository.findByUsername(principal.getUsername()).orElseThrow(()->new SpringRedditException("user with username +"+principal.getUsername()+" no found"));
    }
}
