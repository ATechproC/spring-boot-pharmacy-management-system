package com.atechproc.controller;

import com.atechproc.dto.UserDto;
import com.atechproc.mapper.UserMapper;
import com.atechproc.model.User;
import com.atechproc.request.user.UpdateUserInfoRequest;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.auth.IAuthService;
import com.atechproc.service.pharmacists.IPharmacistsService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IPharmacistsService pharmacistsService;

    @GetMapping("/owner/profile")
    public ResponseEntity<ApiResponse> getOwnerProfile(
            @RequestHeader("Authorization") String jwt
    ) {
        UserDto user = userService.getPharmacyOwnerProfile(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.getUserProfile(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", UserMapper.toDto(user)));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserInfoHandler(
            @RequestBody UpdateUserInfoRequest request,
            @RequestHeader("Authorization") String jwt
    ) {
        UserDto user = userService.updateUserInfo(jwt, request);
        return ResponseEntity.ok(new ApiResponse("Information updated successfully", user));
    }

    @GetMapping("/pharmacists")
    public ResponseEntity<ApiResponse> getPharmacistsHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        List<UserDto> pharmacists = pharmacistsService.getAllPharmacists(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", pharmacists));
    }

    @PutMapping("/pharmacists/update/{pharmacistId}")
    public ResponseEntity<ApiResponse> updatePharmacistInfoHandler(
            @PathVariable Long pharmacistId,
            @RequestBody UpdateUserInfoRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto pharmacist = pharmacistsService.updatePharmacistInfo(pharmacistId, jwt, request);
        return ResponseEntity.ok(new ApiResponse("Pharmacist information updated successfully", pharmacist));
    }

    @DeleteMapping("/pharmacists/delete/{id}")
    public ResponseEntity<ApiResponse> deletePharmacistHandler(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        pharmacistsService.deletePharmacist(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Pharmacist's account deleted successfully", null));
    }

    @PutMapping("/owner/deactivate-account/{userId}")
    public ResponseEntity<ApiResponse> deactivateActivatePharmacistAccountHandler(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDto user = userService.deactivateActivatePharmacistAccount(userId, jwt);
        return ResponseEntity.ok(new ApiResponse("Account status updated successfully", user));
    }
}
