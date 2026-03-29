package com.atechproc.service.pharmacists;

import com.atechproc.dto.UserDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.mapper.UserMapper;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.PharmacyRepository;
import com.atechproc.repository.UserRepository;
import com.atechproc.request.user.UpdateUserInfoRequest;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacistsService implements IPharmacistsService {

    private final IUserService userService;
    private final UserRepository userRepository;
    private final IPharmacyService pharmacyService;
    private final PharmacyRepository pharmacyRepository;

    @Override
    public List<UserDto> getAllPharmacists(String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        List<User> pharmacists = userRepository.findByRoleAndPharmacy_id(USER_ROLE.PHARMACIST, pharmacy.getId());
        return UserMapper.toDTOs(pharmacists);
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public void deletePharmacist(Long id, String jwt) throws Exception {

        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }
        User pharmacist = userService.getUserById(id);
        if (!pharmacist.getRole().equals(USER_ROLE.PHARMACIST) && !pharmacy.getPharmacists().contains(pharmacist)) {
            throw new Exception("You are not allowed to delete this pharmacist's account");
        }
        pharmacy.getPharmacists().remove(pharmacist);
        pharmacyRepository.save(pharmacy);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updatePharmacistInfo(Long id, String jwt, UpdateUserInfoRequest request) throws Exception {
        User pharmacist = userService.getUserById(id);
        Pharmacy pharmacy = pharmacyService.getPharmacyByUser(jwt);

        if (!pharmacy.isOpen()) {
            throw new Exception("You can't achieve this actions because the pharmacy is closed for the moments");
        }
        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        if (!pharmacist.getRole().equals(USER_ROLE.PHARMACIST)
                && !pharmacy.getPharmacists().contains(pharmacist)) {
            throw new Exception("You are not allowed to update this user's information");
        }

        if (request.getName() != null) {
            pharmacist.setUsername(request.getName());
        }

        User savedUer = userRepository.save(pharmacist);

        return UserMapper.toDto(savedUer);
    }
}
