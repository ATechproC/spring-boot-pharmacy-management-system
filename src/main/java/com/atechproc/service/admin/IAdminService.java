package com.atechproc.service.admin;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.response.AuthResponse;

import java.util.List;

public interface IAdminService {
    AuthResponse adminSignup();
    UserDto deactivateActivateUserAccount(Long userId, String jwt, ACCOUNT_STATUS status) throws Exception;
    List<UserDto> getAllUsers(String jwt) throws Exception;
}
