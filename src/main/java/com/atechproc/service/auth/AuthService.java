package com.atechproc.service.auth;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.mapper.UserMapper;
import com.atechproc.model.Cart;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.PharmacyRepository;
import com.atechproc.repository.UserRepository;
import com.atechproc.request.auth.LoginRequest;
import com.atechproc.request.auth.SignupRequest;
import com.atechproc.response.AuthResponse;
import com.atechproc.security.jwt.JwtProvider;
import com.atechproc.security.user.FoodDeliveryUserDetailsService;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final FoodDeliveryUserDetailsService userDetailsService;
    private final IUserService userService;
    private final IPharmacyService pharmacyService;

    @Override
    public AuthResponse signup(SignupRequest request, String ownerJwt) throws Exception {

        User user = userRepository.findByEmail(request.getEmail());
        if (user != null) {
            throw new AlreadyExistsException("Account already exists!");
        }

        if(ownerJwt != null ) {
            User owner = userService.getUserProfile(ownerJwt);
            if(owner.getRole().equals(USER_ROLE.PHARMACIST) || owner.getRole().equals(USER_ROLE.ADMIN)) {
                throw new Exception("You dont have the rights to achieve this actions");
            }
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());

        if(request.getRole().equals(USER_ROLE.PHARMACIST)) {
            Pharmacy pharmacy = pharmacyService.getPharmacyByUser(ownerJwt);
            newUser.setPharmacy(pharmacy);
        }

        if(request.getRole().equals(USER_ROLE.PHARMACY_OWNER)) {
            Cart cart = new Cart();
        }

        // ✅ SAVE USER FIRST
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
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticate(request.getEmail(), request.getPassword());
        User user = userRepository.findByEmail(request.getEmail());
        String jwt = jwtProvider.generateToken(authentication);
        return new AuthResponse(
                "Sign In successfully",
                jwt,
                user.getRole().toString()
        );
    }

    private Authentication authenticate(String email, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(userDetails == null ||
                !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
