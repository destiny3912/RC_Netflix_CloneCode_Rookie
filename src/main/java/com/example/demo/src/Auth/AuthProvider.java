package com.example.demo.src.Auth;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthDao authDao;
    @Autowired
    private final JwtService jwtService;

    public AuthProvider(AuthDao authDao, JwtService jwtService) {
        this.authDao = authDao;
        this.jwtService = jwtService;
    }
}
