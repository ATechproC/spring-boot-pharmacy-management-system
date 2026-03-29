package com.atechproc.service.pharmacists;

import com.atechproc.dto.UserDto;
import com.atechproc.request.user.UpdateUserInfoRequest;

import java.util.List;

public interface IPharmacistsService {
    List<UserDto> getAllPharmacists(String jwt) throws Exception;
//    UserDto togglePharmacistAccountStatus(Long pharmacistId, String jwt) throws Exception;
    void deletePharmacist(Long id, String jwt) throws Exception;
    UserDto updatePharmacistInfo(Long id, String jwt, UpdateUserInfoRequest request) throws Exception;
}
