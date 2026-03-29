package com.atechproc.request.pharmacy;

import com.atechproc.request.address.UpdateAddressRequest;
import lombok.Data;

@Data
public class UpdatePharmacyRequest {
    private String name;
    private String openingTime;
    private String closingTime;
    private UpdateAddressRequest address;
}
