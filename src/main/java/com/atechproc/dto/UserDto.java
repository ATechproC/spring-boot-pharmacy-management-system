package com.atechproc.dto;
import com.atechproc.enums.ACCOUNT_STATUS;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private ACCOUNT_STATUS status;
    private String year;
}
