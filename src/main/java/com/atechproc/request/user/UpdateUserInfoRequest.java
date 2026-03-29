package com.atechproc.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserInfoRequest {

    @NotBlank(message = "New name is required")
    private String name;
}
