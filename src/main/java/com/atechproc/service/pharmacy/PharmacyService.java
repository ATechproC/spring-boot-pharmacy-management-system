package com.atechproc.service.pharmacy;

import com.atechproc.dto.PharmacyDto;
import com.atechproc.enums.ACCOUNT_STATUS;
import com.atechproc.enums.USER_ROLE;
import com.atechproc.exception.AlreadyExistsException;
import com.atechproc.exception.ResourceNotFoundException;
import com.atechproc.mapper.PharmacyMapper;
import com.atechproc.model.Address;
import com.atechproc.model.Cart;
import com.atechproc.model.Pharmacy;
import com.atechproc.model.User;
import com.atechproc.repository.AddressRepository;
import com.atechproc.repository.CartRepository;
import com.atechproc.repository.PharmacyRepository;
import com.atechproc.request.pharmacy.CreatePharmacyRequest;
import com.atechproc.request.pharmacy.UpdatePharmacyRequest;
import com.atechproc.service.business.IBusinessLogicService;
import com.atechproc.service.medicine.IMedicineService;
import com.atechproc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyService implements IPharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final IUserService userService;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final IBusinessLogicService businessLogicService;

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public PharmacyDto createPharmacy(CreatePharmacyRequest request, String jwt) throws Exception {

        User user = userService.getUserProfile(jwt);

        if (user.getStatus().equals(ACCOUNT_STATUS.REFUSED)) {
            throw new Exception("You cant achieve this actions because your account is REFUSED");
        }

        if (user.getStatus().equals(ACCOUNT_STATUS.PENDING)) {
            throw new Exception("You cant achieve this actions because your account is PENDING");
        }

        Address existingAddress = addressRepository.findByCityAndCountryAndStreet(
                request.getAddress().getCity(),
                request.getAddress().getCountry(),
                request.getAddress().getStreet());

        if (existingAddress != null) {
            throw new AlreadyExistsException("Pharmacy address already exists");
        }

        Address address = new Address();
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setCountry(request.getAddress().getCountry());
        address.setPharmacy(null);

        Address savedAddress = addressRepository.save(address);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName(request.getName());
        pharmacy.setOpeningTime(LocalTime.parse(request.getOpeningTime()));
        pharmacy.setClosingTime(LocalTime.parse(request.getClosingTime()));
        pharmacy.setOwner(user);
        pharmacy.setCart(null);
        pharmacy.setAddress(savedAddress);

        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        savedAddress.setPharmacy(savedPharmacy);
        addressRepository.save(savedAddress);

        Cart cart = new Cart();
        cart.setPharmacy(savedPharmacy);
        Cart savedCart = cartRepository.save(cart);

        pharmacy.setCart(savedCart);
        Pharmacy pharmacy1 = pharmacyRepository.save(pharmacy);

        return PharmacyMapper.toDto(pharmacy1, businessLogicService);
    }

    @Override
    public Pharmacy getPharmacyById(Long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found with id " + pharmacyId));
    }

    @Override
    @PreAuthorize("hasRole('PHARMACY_OWNER')")
    public PharmacyDto updatePharmacyByOwner(UpdatePharmacyRequest request, String jwt) throws Exception {

        Pharmacy pharmacy = getPharmacyByUser(jwt);

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

        Pharmacy existingPharmacy = pharmacyRepository.findByOwner_id(user.getId());

        if (existingPharmacy == null) {
            throw new ResourceNotFoundException("Pharmacy not found");
        }

        if (request.getName() != null) {
            existingPharmacy.setName(request.getName());
        }

        if (request.getOpeningTime() != null) {
            existingPharmacy.setOpeningTime(LocalTime.parse(request.getOpeningTime()));
        }

        if (request.getClosingTime() != null) {
            existingPharmacy.setClosingTime(LocalTime.parse(request.getClosingTime()));
        }

        if (request.getAddress() != null) {
            if (request.getAddress().getCity() != null) {
                existingPharmacy.getAddress().setCity(request.getAddress().getCity());
            }

            if (request.getAddress().getCountry() != null) {
                existingPharmacy.getAddress().setCountry(request.getAddress().getCountry());
            }

            if (request.getAddress().getStreet() != null) {
                existingPharmacy.getAddress().setStreet(request.getAddress().getStreet());
            }
        }

        Pharmacy savedPharmacy = pharmacyRepository.save(existingPharmacy);

        return PharmacyMapper.toDto(savedPharmacy, businessLogicService);
    }

    @Override
    @PreAuthorize("hasAnyRole('PHARMACY_OWNER')")
    public PharmacyDto updatePharmacyStatus(String jwt) throws Exception {

        Pharmacy pharmacy = getPharmacyByUser(jwt);

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

        Pharmacy existingPharmacy = getPharmacyByUser(jwt);

        if (existingPharmacy == null) {
            throw new ResourceNotFoundException("Pharmacy not found");
        }

        existingPharmacy.setOpen(!existingPharmacy.isOpen());

        Pharmacy savedPharmacy = pharmacyRepository.save(existingPharmacy);

        return PharmacyMapper.toDto(savedPharmacy, businessLogicService);
    }

    @Override
    public List<PharmacyDto> getAllPharmacies() {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        return PharmacyMapper.toDTOs(pharmacies, businessLogicService);
    }

    @Override
    public Pharmacy getPharmacyByUser(String jwt) {
        User user = userService.getUserProfile(jwt);
        if (user.getRole().equals(USER_ROLE.PHARMACIST)) {
            return user.getPharmacy();
        }
        return pharmacyRepository.findByOwner_id(user.getId());
    }

}
