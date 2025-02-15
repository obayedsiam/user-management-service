package com.assignment.user.services;

import com.assignment.user.request.LoginRequest;
import com.assignment.user.response.JwtResponse;
import com.assignment.user.response.Response;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<JwtResponse> login(LoginRequest loginRequest);
    Response getUserInfo() throws BadRequestException;

}
