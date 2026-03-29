package com.atechproc.mapper;

import com.atechproc.dto.UserDto;
import com.atechproc.model.User;

import java.util.List;

public class UserMapper {
    public static UserDto toDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        userDto.setStatus(user.getStatus());
        userDto.setYear(String.valueOf(user.getCreatedAt().getYear()));

        return userDto;
    }

    public static List<UserDto> toDTOs(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto).toList();
    }
}
