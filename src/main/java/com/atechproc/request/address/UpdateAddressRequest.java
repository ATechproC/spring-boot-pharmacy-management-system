package com.atechproc.request.address;

import lombok.Data;

@Data
public class UpdateAddressRequest {
    private String street;
    private String city;
    private String country;
}
