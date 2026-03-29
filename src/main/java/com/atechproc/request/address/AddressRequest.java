package com.atechproc.request.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "Pharmacy street is required")
    private String street;

    @NotBlank(message = "Pharmacy city is required")
    private String city;

    @NotBlank(message = "Pharmacy country is required")
    private String country;
}
