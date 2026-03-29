package com.atechproc.repository;

import com.atechproc.model.Address;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByCityAndCountryAndStreet(String city, String country, String street);
}
