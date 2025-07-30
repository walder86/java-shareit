package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotNull(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Некорректный email")
    private String email;
}
