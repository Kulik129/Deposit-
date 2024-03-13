package ru.kulik.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDTO {
    private Long id;
    private UUID userUUID;
    @NotBlank(message = "Поле не может быть пустым или содержать пробелы")
    @Size(min = 6, max = 150,message = "Минимальная длинна строки должна быть не меньше 6 символов, а максимальная длина не должна превышать 150 символов.")
    private String fullName;
    private int totalBalance;
    private int balance;
    private List<AccountDTO> account;
}
