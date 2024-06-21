package com.anurag.application.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.application.dao.exception.DuplicateKeyException;
import com.anurag.application.dao.request.SignUpRequest;
import com.anurag.application.dao.request.SigninRequest;
import com.anurag.application.dao.response.JwtAuthenticationResponse;
import com.anurag.application.entities.Role;
import com.anurag.application.entities.User;
import com.anurag.application.repository.UserRepository;
import com.anurag.application.service.AuthenticationService;
import com.anurag.application.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException("A user with this username already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateKeyException("A user with this email already exists.");
        }
        if (userRepository.existsByMobileNo(request.getMobile())) {
            throw new DuplicateKeyException("A user with this mobile number already exists.");
        }

        var user = User.builder().name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .mobileNo(request.getMobile()).username(request.getUsername())
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
