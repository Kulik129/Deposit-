package ru.kulik.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kulik.aspect.AccountValidation;
import ru.kulik.aspect.UserNotNull;
import ru.kulik.aspect.UserValidation;
import ru.kulik.dto.AccountDTO;
import ru.kulik.dto.TransactionType;
import ru.kulik.dto.UserDTO;
import ru.kulik.models.Account;
import ru.kulik.models.User;
import ru.kulik.repository.AccountRepository;
import ru.kulik.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImp implements CRUDService<UserDTO>, Balance<UserDTO> {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @UserNotNull
    @Override
    public void save(UserDTO data) {
        userRepository.save(User.builder()
                .uuid(UUID.randomUUID())
                .fullName(data.getFullName())
                .build());
    }
    @Override
    @UserValidation
    public UserDTO findByUUID(UUID uuid) {
        Optional<User> user = userRepository.findByUuid(uuid);
        List<AccountDTO> accounts = user.get().getAccounts().stream()
                .map(it -> AccountDTO.builder()
                        .id(it.getId())
                        .accountUUID(it.getUuid())
                        .balance(it.getBalance())
                        .build())
                .collect(Collectors.toList());

        return UserDTO.builder()
                .id(user.get().getId())
                .userUUID(user.get().getUuid())
                .fullName(user.get().getFullName())
                .totalBalance(accounts.stream()
                        .mapToInt(it -> it.getBalance())
                        .sum())
                .account(accounts)
                .build();
    }
    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(it -> {
                    int totalBalance = it.getAccounts().stream()
                            .mapToInt(at -> at.getBalance())
                            .sum();

                    return UserDTO.builder()
                            .id(it.getId())
                            .userUUID(it.getUuid())
                            .fullName(it.getFullName())
                            .totalBalance(totalBalance)
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    @UserValidation
    @Override
    public void delete(UUID uuid) {
        userRepository.deleteByUuid(uuid);
    }
    @Transactional
    @AccountValidation(uuidArgIndex = 2)
    @Override
    public UserDTO updateBalance(int amount, TransactionType transactionType, UUID uuid) {
        Optional<Account> account = accountRepository.findByUuid(uuid);

        switch (transactionType) {
            case DEPOSIT -> {
                int balance = account.get().getBalance();
                account.get().setBalance(balance + amount);
                accountRepository.save(account.get());
            }
            case WITHDRAW -> {
                int balance = account.get().getBalance();
                if (balance >= amount) {
                    account.get().setBalance(balance - amount);
                    accountRepository.save(account.get());
                } else {
                    throw new RuntimeException("Не достаточно денег на счету");
                }
            }
        }

        return UserDTO.builder()
                .fullName(account.get().getOwner().getFullName())
                .totalBalance(account.get().getOwner().getAccounts().stream()
                        .mapToInt(it -> it.getBalance())
                        .sum())
                .balance(account.get().getBalance())
                .build();
    }

    @Override
    @AccountValidation
    public UserDTO getBalance(UUID accountUUID) {
        Optional<Account> account = accountRepository.findByUuid(accountUUID);
        return UserDTO.builder()
                .fullName(account.get().getOwner().getFullName())
                .totalBalance(account.get().getBalance())
                .build();
    }

    @UserValidation
    public List<AccountDTO> getAccounts(UUID uuid) {
        Optional<User> user = userRepository.findByUuid(uuid);
        List<Account> accounts = user.get().getAccounts();

        List<AccountDTO> accountDTOs = accounts.stream()
                .map(it -> AccountDTO.builder()
                        .id(it.getId())
                        .accountUUID(it.getUuid())
                        .balance(it.getBalance())
                        .owner(UserDTO.builder()
                                .id(user.get().getId())
                                .userUUID(user.get().getUuid())
                                .fullName(user.get().getFullName())
                                .build())
                        .build())
                .collect(Collectors.toList());
        return accountDTOs;
    }

    @Transactional
    @UserValidation
    public void addNewAccountForUser(UUID uuid) {
        Optional<User> user = userRepository.findByUuid(uuid);
        Account account = Account.builder()
                .uuid(UUID.randomUUID())
                .owner(user.get())
                .build();
        user.get().getAccounts().add(account);
        accountRepository.save(account);
    }

    @Transactional
    @UserNotNull
    public UserDTO createNewUser(UserDTO userDTO) {
        User user = User.builder().uuid(UUID.randomUUID()).fullName(userDTO.getFullName()).build();
        Account account = Account.builder().uuid(UUID.randomUUID()).owner(user).build();

        user.setAccounts(new ArrayList<>(Collections.singletonList(account)));

        userRepository.save(user);
        accountRepository.save(account);

        return UserDTO.builder()
                .userUUID(user.getUuid())
                .fullName(user.getFullName())
                .totalBalance(account.getBalance())
                .account(getAccounts(user.getUuid()))
                .build();
    }
}