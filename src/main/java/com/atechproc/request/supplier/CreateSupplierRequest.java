package com.atechproc.request.supplier;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String name;
}
