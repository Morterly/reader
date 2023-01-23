package com.reader.mapper;


import com.reader.dto.UserCreateEditDto;
import com.reader.entity.Role;
import com.reader.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {
    @Override
    public User map(UserCreateEditDto object) {
        Role role = Optional.ofNullable(object.getRole()).orElse(Role.USER);
        return User.builder()
                .id(object.getId())
                .username(object.getUsername())
                .password(object.getPassword())
                .email(object.getEmail())
                .role(role)
                .build();
    }

    public UserCreateEditDto mapFrom(User user) {
        return UserCreateEditDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
