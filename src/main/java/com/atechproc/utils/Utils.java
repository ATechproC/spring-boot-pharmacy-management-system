package com.atechproc.utils;

import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.service.pharmacy.IPharmacyService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Utils {

    private final IPharmacyService pharmacyService;
    private final IUserService userService;

    public Pharmacy checkPharmacyAndUserStatus(String jwt) throws Exception {
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

        return pharmacy;
    }
}
