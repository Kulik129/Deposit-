package ru.kulik.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kulik.dto.UpdateBalance;
import ru.kulik.dto.UserDTO;
import ru.kulik.servise.UserServiceImp;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImp service;

    @PutMapping()
    @Operation(summary = "Обновление баланса.", description = "Полнение и снятие ДС со счета.")
    public ResponseEntity<UserDTO> updateBalance(@Valid @RequestBody UpdateBalance request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateBalance(request.getAmount(), request.getTransactionType(), request.getAccountUUID()));
    }

    @GetMapping("/balance/{accountUUID}")
    @Operation(summary = "Получить баланс кошелька.", description = "Получить баланс кошелька по UUID.")
    public ResponseEntity<UserDTO> getBalance(@PathVariable UUID accountUUID) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getBalance(accountUUID));
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя со счетом.", description = "Создание нового пользователя с привязанным к нему счетом.")
    public ResponseEntity<UserDTO> saveNewUser(@Valid @RequestBody UserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNewUser(request));
    }

    @PostMapping("/without-a-bill")
    @Operation(summary = "Создать нового пользователя без счета.", description = "Будет создан новый пользователь без счета.")
    public ResponseEntity<List<UserDTO>> createUserWithoutAnAccount(@Valid @RequestBody UserDTO request) {
        service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findAll());
    }

    @PutMapping("/add/{uuid}")
    @Operation(summary = "Добавить новый счет для существующего пользователя.", description = "Потребуется uuid пользователя.")
    public ResponseEntity<?> addNewAccount(@PathVariable UUID uuid) {
        service.addNewAccountForUser(uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.getAccounts(uuid));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Удаление пользователя по его uuid.", description = "При удалении пользователя так же будут удалены все его счета.")
    public ResponseEntity<List<UserDTO>> deleteUserByUUID(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/users")
    @Operation(summary = "Получение всех пользователй.", description = "Описание всех пользоватлей с общей суммой на балансе всех счетов.")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Получение пользователя по его uuid пользователя.", description = "Получение пользователя.")
    public ResponseEntity<UserDTO> getUserByUUID(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByUUID(uuid));
    }
}
