package ru.kulik.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateBalance {
    @NotNull(message = "Поле не может быть пустым.")
    private UUID accountUUID;
    @NotNull(message = "Обязательно укажите тип транзакции. DEPOSIT если хотите пополнить баланс и WITHDRAW если хотите снять деьги со счета.")
    private TransactionType transactionType;
    @Min(value = 10, message = "Минимальная сумма пополнения не может быть меьше 10 руб.")
    @Max(value = 15000, message = "Максимальная сумма пополнения не может превышать 15 000 руб.")
    private int amount;
}
