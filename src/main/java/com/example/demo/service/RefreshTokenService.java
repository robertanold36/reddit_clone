package com.example.demo.service;

import com.example.demo.exception.SpringRedditException;
import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenService {

    private  final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        String token= UUID.randomUUID().toString();
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setToken(token);

       return refreshTokenRepository.save(refreshToken);
    }

    void verifyRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new SpringRedditException("refresh token is invalid"));
    }

}
