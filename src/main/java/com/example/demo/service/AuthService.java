package com.example.demo.service;


import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.Person;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
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
                        "Please activate the email",person.getEmail(),"Please click the link " +
                        "below to activate your email :" +
                        "http://localhost:8081/api/auth/account/verification/"+token
                )
        );
    }

    @Transactional
    public String generateVerificationToken(Person person){
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setPerson(person);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
