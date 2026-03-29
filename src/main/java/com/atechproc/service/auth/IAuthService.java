package com.atechproc.service.auth;

import com.atechproc.dto.UserDto;
import com.atechproc.request.auth.LoginRequest;
import com.atechproc.request.auth.SignupRequest;
import com.atechproc.response.AuthResponse;

import java.util.List;

public interface IAuthService {
    AuthResponse signup(SignupRequest request, String jwt) throws Exception;
    AuthResponse login(LoginRequest request);
}
