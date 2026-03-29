package com.atechproc.service.user;

import com.atechproc.dto.UserDto;
import com.atechproc.model.User;
import com.atechproc.request.user.UpdateUserInfoRequest;

import java.util.List;

public interface IUserService {
    User getUserProfile(String jwt);
    User getUserByEmail(String email);
    User getUserById(Long userId);
    UserDto updateUserInfo(String jwt, UpdateUserInfoRequest request);
    UserDto getPharmacyOwnerProfile(String jwt);
    User saveUser(User user);
    UserDto deactivateActivatePharmacistAccount(Long userId, String jwt) throws Exception;
}
