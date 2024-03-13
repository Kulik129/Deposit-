package ru.kulik.servise;

import ru.kulik.dto.TransactionType;

import java.util.UUID;

public interface Balance<T> {
    T updateBalance(int amount, TransactionType transactionType, UUID uuid);
    T getBalance(UUID uuid);
}
