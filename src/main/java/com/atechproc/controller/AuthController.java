package com.atechproc.controller;

import com.atechproc.request.auth.LoginRequest;
import com.atechproc.request.auth.SignupRequest;
import com.atechproc.response.AuthResponse;
import com.atechproc.service.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
            @Valid
            @RequestBody
            SignupRequest request,
            @RequestHeader(value = "Authorization", required = false) String jwt
    ) throws Exception {
        AuthResponse response = authService.signup(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @Valid
            @RequestBody
            LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
