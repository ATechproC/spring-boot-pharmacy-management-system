package com.atechproc.controller;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.response.ApiResponse;
import com.atechproc.response.AuthResponse;
import com.atechproc.service.admin.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin")
public class AdminController {

    private final IAdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> adminSignupHandler() {
        AuthResponse response = adminService.adminSignup();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<UserDto> users = adminService.getAllUsers(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", users));
    }

    @PutMapping("/deactivate-account/{userId}")
    public ResponseEntity<ApiResponse> deactivateUserAccountHandler(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt,
            @RequestParam ACCOUNT_STATUS status
            ) throws Exception {
        UserDto user = adminService.deactivateActivateUserAccount(userId, jwt, status);
        return ResponseEntity.ok(new ApiResponse("Account status updated successfully", user));
    }

}
