package com.example.demo.service;

import com.example.demo.exception.SpringRedditException;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsImplService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(s).orElseThrow(() ->
                new SpringRedditException("person not found with username " + s));

        return new User(person.getUsername(),person.getPassword(),person.isEnabled(),
                true,true,
                true,getAuthorities("USER"));

    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role){
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }
}
