package com.atechproc.service.admin;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.mapper.UserMapper;
import com.atechproc.model.Cart;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.UserRepository;
import com.atechproc.request.auth.SignupRequest;
import com.atechproc.response.AuthResponse;
import com.atechproc.security.jwt.JwtProvider;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    @Value("${admin_username}")
    private String adminUsername;

    @Value("${admin_email}")
    private String adminEmail;

    @Value("${admin_password}")
    private String adminPassword;

    private final IUserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public AuthResponse adminSignup() {

        User newUser = new User();
        newUser.setUsername(adminUsername);
        newUser.setEmail(adminEmail);
        newUser.setPassword(passwordEncoder.encode(adminPassword));
        newUser.setRole(USER_ROLE.ADMIN);
        newUser.setStatus(ACCOUNT_STATUS.ACCEPTED);

        User savedUser = userRepository.save(newUser);

        // ✅ CREATE REAL AUTHORITIES
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name())
        );

        // ✅ CREATE AUTHENTICATION WITH ROLE
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        savedUser.getEmail(),
                        null,
                        authorities
                );

        // ✅ GENERATE JWT WITH ROLES
        String jwt = jwtProvider.generateToken(authentication);

        return new AuthResponse(
                "Account created successfully",
                jwt,
                savedUser.getRole().toString()
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto deactivateActivateUserAccount(Long userId, String jwt, ACCOUNT_STATUS status) throws Exception {

        User admin = userService.getUserProfile(jwt);

        if(admin.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if(admin.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        User user = userService.getUserById(userId);

        if(user.getRole().equals(USER_ROLE.ADMIN)) {
            throw new Exception("You cannot update this user's account status");
        }

        user.setStatus(status);
        User savedUser = userService.saveUser(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers(String jwt) throws Exception {

        User admin = userService.getUserProfile(jwt);

        if(admin.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if(admin.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        List<User> users = userRepository.findByRole(USER_ROLE.PHARMACY_OWNER);
        return UserMapper.toDTOs(users);
    }
}
