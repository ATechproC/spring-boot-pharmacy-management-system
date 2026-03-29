package com.atechproc.service.user;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.UserMapper;
import com.atechproc.model.User;
import com.atechproc.repository.UserRepository;
import com.atechproc.request.user.UpdateUserInfoRequest;
import com.atechproc.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User getUserProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getUserByEmail(email);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public UserDto deactivateActivatePharmacistAccount(Long userId, String jwt) throws Exception {
        // Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        User owner = getUserProfile(jwt);

        if (owner.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (owner.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        User user = getUserById(userId);

        if (user.getRole().equals(USER_ROLE.PHARMACY_OWNER) || user.getRole().equals(USER_ROLE.ADMIN)) {
            throw new Exception("You don't have the rights to update this status account");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            user.setStatus(ACCOUNT_STATUS.ACCEPTED);
        } else
            user.setStatus(ACCOUNT_STATUS.PENDING);

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email " + email);
        }
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public UserDto updateUserInfo(String jwt, UpdateUserInfoRequest request) {
        User user = getUserProfile(jwt);
        if (request.getName() != null) {
            user.setUsername(request.getName());
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserDto getPharmacyOwnerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = getUserByEmail(email);
        if (user.getRole().equals(USER_ROLE.PHARMACIST)) {
            user = user.getPharmacy().getOwner();
        }
        return UserMapper.toDto(user);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
