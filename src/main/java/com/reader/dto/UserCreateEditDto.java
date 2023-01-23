package com.reader.dto;

import com.reader.entity.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class UserCreateEditDto {
    Long id;
    @Size(min = 4, max = 10, message = "Имя пользователя должно находиться в диапазоне от 4 до 10 символов")
    String username;
    @Size(min = 7, max = 15, message = "Пароль должен находиться в диапазоне от 7 до 15 символов")
    String password;
    @NotBlank(message = "Email не может быть пустым")
    @Email
    String email;
    Role role;
}
