package com.anurag.application.service;

import com.anurag.application.dao.request.SignUpRequest;
import com.anurag.application.dao.request.SigninRequest;
import com.anurag.application.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
