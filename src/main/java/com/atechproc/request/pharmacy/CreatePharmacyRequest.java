package com.atechproc.request.pharmacy;

import com.atechproc.request.address.AddressRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePharmacyRequest {

    @NotBlank(message = "Pharmacy name is required")
    private String name;

    @NotBlank(message = "Pharmacy opening time is required")
    private String openingTime;

    @NotBlank(message = "Pharmacy closing time is required")
    private String closingTime;

    @NotNull(message = "Pharmacy address is required")
    private AddressRequest address;
}
